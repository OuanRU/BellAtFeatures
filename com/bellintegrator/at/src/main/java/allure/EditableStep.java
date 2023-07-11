package allure;

import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditableStep {
    private final String stepId;
    protected StepResult stepResult;
    private Status status;
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

    protected void setThisStepStatusFailed() {
        this.status = Status.FAILED;
        dataTransfer.addStepToEditedMap(this);
    }

    protected void setStepStatusFailed() {
        this.status = Status.FAILED;
        for (EditableStep step : dataTransfer.getAllOpenedSteps()) {
            step.setThisStepStatusFailed();
        }
        dataTransfer.addStepToEditedMap(this);
    }

    protected void setStepName(String newStepName) {
        this.stepResult = stepResult.setName(newStepName);
    }

    protected void deleteStepParameter(String parameterName) {
        StepResult result = this.stepResult;
        List<Parameter> parameters = result.getParameters();
        parameters = parameters.stream().filter(parameter -> !parameter.getName().equals(parameterName)).collect(Collectors.toList());
        result.setParameters(parameters);
        this.stepResult = result;
        this.edit();
    }

    protected void deleteAllStepParameters() {
        StepResult result = this.stepResult;
        List<Parameter> parameters = new ArrayList<>();
        result.setParameters(parameters);
        this.stepResult = result;
        this.edit();
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
        this.edit();
    }

    protected void addStepParameter(String parameterName, String parameterValue) {
        StepResult result = this.stepResult;
        List<Parameter> parameters = result.getParameters();
        parameters.add(new Parameter().setName(parameterName).setValue(parameterValue));
        result.setParameters(parameters);
        this.stepResult = result;
        this.edit();
    }

    protected void edit() {
        AllureEdit.replaceStepFromAllureReport(this.stepId, this.stepResult);
    }

    protected void editFromDelayedStep(DelayedEditableStep delayedEditableStep) {
        if (delayedEditableStep.isStepNameEdited()) {
            setStepName(delayedEditableStep.getStepName());
        }
        if (delayedEditableStep.isStepParametersEdited()) {
            for (Map.Entry<String, String> paramEditEntry : delayedEditableStep.getEditedParameters().entrySet()) {
                editStepParameterName(paramEditEntry.getKey(), paramEditEntry.getValue());
            }
        }
        if (delayedEditableStep.isStepParametersAdded()) {
            for (Parameter addedParameter : delayedEditableStep.getAddedParameters()) {
                addStepParameter(addedParameter.getName(), addedParameter.getValue());
            }
        }
        AllureEdit.replaceStepFromAllureReport(this.stepId, this.stepResult);
    }

    protected void editStatusIfNecessary() {
        if (this.status.equals(Status.FAILED)) {
            this.stepResult.setStatus(Status.FAILED);
        }
        dataTransfer.removeEditedStep(this.getStepId());
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
