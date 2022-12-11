package allure.edit;

import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditableStep {
    private String stepId;
    private StepResult stepResult;
    AllureStepDataTransfer dataTransfer = AllureStepDataTransfer.getInstance();

    protected EditableStep(String stepId, StepResult stepResult) {
        this.stepId = stepId;
        this.stepResult = stepResult;
    }

    protected String getStepId() {
        return stepId;
    }

    protected StepResult getStepResult() {
        return stepResult;
    }

    protected void setStepStatusFailed() {
        this.stepResult = stepResult.setStatus(Status.FAILED);
        dataTransfer.addStepToEditedMap(this);
    }

    protected void setStepName(String newStepName) {
        this.stepResult = stepResult.setName(newStepName);
        dataTransfer.addStepToEditedMap(this);
    }

    protected void deleteStepParameter(String parameterName) {
        StepResult result = this.stepResult;
        List<Parameter> parameters = result.getParameters();
        parameters = parameters.stream().filter(parameter -> !parameter.getName().equals(parameterName)).collect(Collectors.toList());
        result.setParameters(parameters);
        this.stepResult = result;
        dataTransfer.addStepToEditedMap(this);
    }

    protected void deleteStepAllParameters() {
        StepResult result = this.stepResult;
        List<Parameter> parameters = new ArrayList<>();
        result.setParameters(parameters);
        this.stepResult = result;
        dataTransfer.addStepToEditedMap(this);
    }

    protected void editStepParameterName(String oldParameterName, String newParameterName) {
        StepResult result = this.stepResult;
        List<Parameter> parameters = result.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.getName().equals(oldParameterName)) {
                parameter.setName(newParameterName);
            }
        }
        result.setParameters(parameters);
        this.stepResult = result;
        dataTransfer.addStepToEditedMap(this);
    }

    protected void edit() {
        AllureEdit.replaceStepFromAllureReport(stepId, stepResult);
        dataTransfer.removeEditedStep(stepId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditableStep that = (EditableStep) o;
        return stepId.equals(that.stepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepId);
    }
}
