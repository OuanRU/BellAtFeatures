package allure.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class AllureStepDataTransfer {
    private static AllureStepDataTransfer instance;

    // Мапа, в которой храняться шаги, у которых менялся статус
    private HashMap<String, EditableStep> editedSteps;
    // Стек открытых шагов, благодаря которому можно найти текущий исполняемый шаг
    private Stack<EditableStep> stepStack;
    // Последний завершённый шаг
    private EditableStep lastStep;
    // Редактируемый следующий шаг
    private DelayedEditableStep nextStep;

    private AllureStepDataTransfer() {
        editedSteps = new HashMap<>();
        stepStack = new Stack<>();
    }

    protected static AllureStepDataTransfer getInstance() {
        if (instance == null) {
            instance = new AllureStepDataTransfer();
        }
        return instance;
    }

    protected EditableStep getLastStep(){
        return this.lastStep;
    }

    protected void setLastStep(EditableStep lastStep) {
        this.lastStep = lastStep;
    }

    protected void addStepToStack(EditableStep step) {
        this.stepStack.push(step);
    }

    protected void popStepFromStack() {
        this.stepStack.pop();
    }

    protected EditableStep getCurrentStep() {
        return this.stepStack.peek();
    }

    protected void addStepToEditedMap(EditableStep step) {
        editedSteps.put(step.getStepId(), step);
    }

    protected boolean isStepWasEdited(String uuid) {
        return editedSteps.containsKey(uuid);
    }

    protected EditableStep getEditedStep(String stepId) {
        return editedSteps.get(stepId);
    }

    protected void removeEditedStep(String uuid) {
        this.editedSteps.remove(uuid);
    }

    protected List<EditableStep> getAllOpenedSteps() {
        return new ArrayList<>(stepStack);
    }
}
