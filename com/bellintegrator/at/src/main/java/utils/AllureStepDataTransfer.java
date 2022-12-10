package utils;

import io.qameta.allure.model.StepResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class AllureStepDataTransfer {
    private static AllureStepDataTransfer instance;

    private Map<String, StepResult> stepResultsMap;
    private Stack<EditableStep> stepStack;
    private EditableStep lastStep;

    private AllureStepDataTransfer() {
        stepResultsMap = new HashMap<>();
        stepStack = new Stack<>();
    }

    public static AllureStepDataTransfer getInstance() {
        if (instance == null) {
            instance = new AllureStepDataTransfer();
        }
        return instance;
    }

    public String getLastStepUuid() {
        return this.lastStep.getStepId();
    }

    public StepResult getLastStepResult() {
        return this.lastStep.getStepResult();
    }

    public void setLastStep(EditableStep lastStep) {
        this.lastStep = lastStep;
    }

    public void addStepToStack(EditableStep step) {
        this.stepStack.push(step);
    }

    public void popStepFromStack() {
        this.stepStack.pop();
    }

    public String getCurrentStepUuid() {
        return this.stepStack.peek().getStepId();
    }

    public StepResult getCurrentStepResult() {
        return this.stepStack.peek().getStepResult();
    }
}
