package utils;

import io.qameta.allure.model.StepResult;

public class EditableStep {
    private String stepId;
    private StepResult stepResult;

    public EditableStep(String stepId, StepResult stepResult) {
        this.stepId = stepId;
        this.stepResult = stepResult;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public StepResult getStepResult() {
        return stepResult;
    }

    public void setStepResult(StepResult stepResult) {
        this.stepResult = stepResult;
    }
}
