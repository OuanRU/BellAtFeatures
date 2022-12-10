package listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import utils.AllureStepDataTransfer;
import utils.EditableStep;

public class AllureStepListener implements StepLifecycleListener {
    private AllureStepDataTransfer data = AllureStepDataTransfer.getInstance();

    @Override
    public void afterStepStart(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        this.data.setLastStep(new EditableStep(uuid, result));
        this.data.addStepToStack(new EditableStep(uuid, result));
    }

    @Override
    public void afterStepUpdate(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        this.data.setLastStep(new EditableStep(uuid, result));
    }

    @Override
    public void beforeStepStop(StepResult result) {
        String uuid = Allure.getLifecycle().getCurrentTestCaseOrStep().get();
        this.data.setLastStep(new EditableStep(uuid, result));
    }

    @Override
    public void afterStepStop(StepResult result) {
        this.data.popStepFromStack();
    }
}
