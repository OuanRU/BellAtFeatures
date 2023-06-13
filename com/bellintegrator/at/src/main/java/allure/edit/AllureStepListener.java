package allure.edit;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;

public class AllureStepListener implements StepLifecycleListener {
    private final AllureStepDataTransfer data = AllureStepDataTransfer.getInstance();

    @Override
    public void afterStepStart(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        this.data.addStepToStack(new EditableStep(uuid, result));
        if (DelayedEditableStep.getInstance().isDelayedEdit()) {
            data.getCurrentStep().editFromDelayedStep(DelayedEditableStep.getInstance());
            DelayedEditableStep.getInstance().clearDelayedStep();
        }
    }

    /**
     * Update - это функция, которая вызывается только для изменения статуса шага(А точнее для инициализации статуса).
     * Остальные параметры она не трогает.
     */
    @Override
    public void afterStepUpdate(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        // Применяем изменения, если они были внесены
        if (data.isStepWasEdited(uuid)) {
            data.getEditedStep(uuid).edit();
        }
        // Заменяем изменённый шаг, в т.ч. с изменениями в ходе его выполнения
        this.data.popStepFromStack();
        this.data.addStepToStack(new EditableStep(uuid, result));
    }

    @Override
    public void beforeStepStop(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        // TODO возможно оптимизировать?
        // Изменение статуса шага на failed, если функция делающая это вызывалась
        if (data.isStepWasEdited(uuid)) {
            data.getEditedStep(uuid).editStatusIfNecessary();
        }
        this.data.setLastStep(new EditableStep(uuid, result));
    }

    @Override
    public void afterStepStop(StepResult result) {
        this.data.popStepFromStack();
    }
}
