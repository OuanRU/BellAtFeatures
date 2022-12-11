package allure.edit;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;

public class AllureStepListener implements StepLifecycleListener {
    private AllureStepDataTransfer data = AllureStepDataTransfer.getInstance();

    @Override
    public void afterStepStart(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        this.data.addStepToStack(new EditableStep(uuid, result));
    }

    @Override
    public void afterStepUpdate(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        // Применяем изменения, если они были внесены
        if (data.isStepWasEdited(uuid)) {
            data.getEditedStep(uuid).edit();
        }
        // Заменяем изменённый шаг, в т.ч. с изменениями в ходе его выполнения
        // TODO затестить возможность вызова данного метода не на последний шаг
        this.data.popStepFromStack();
        this.data.addStepToStack(new EditableStep(uuid, result));
    }

    @Override
    public void beforeStepStop(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        this.data.setLastStep(new EditableStep(uuid, result));
        if (data.isStepWasEdited(uuid)) {
            data.getEditedStep(uuid).edit();
        }
    }

    @Override
    public void afterStepStop(StepResult result) {
        this.data.popStepFromStack();
    }
}
