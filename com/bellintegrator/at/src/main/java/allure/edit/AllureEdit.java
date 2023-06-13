package allure.edit;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.internal.AllureStorage;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.StepResult;

import java.lang.reflect.Field;
import java.util.Map;

public class AllureEdit {
    private final static AllureStepDataTransfer dataTransfer = AllureStepDataTransfer.getInstance();

    /**
     * @param uuid   - id заменяемого шага, получается с помощью AllureStepListener и передаётся через AllureStepDataTransfer
     * @param result - stepResult который записывается в отчёт вместо существующего
     * @author Victor Avdonin
     * Основной метод замены информации в аллюр отчёте.
     * При помощи рефлексии получает доступ к хранилищу информации о шагах в аллюре,
     * Перезаписывает хранящийся StepResult на изменённый.
     */
    protected static void replaceStepFromAllureReport(String uuid, StepResult result) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        AllureStorage lifecycleStorage = null;
        try {
            Field lifecycleStorageField = lifecycle.getClass().getDeclaredField("storage");
            lifecycleStorageField.setAccessible(true);
            lifecycleStorage = (AllureStorage) lifecycleStorageField.get(lifecycle);
            Field allureStorageField = lifecycleStorage.getClass().getDeclaredField("storage");
            allureStorageField.setAccessible(true);
            Map<String, Object> allureStorage = (Map<String, Object>) allureStorageField.get(lifecycleStorage);
            allureStorage.replace(uuid, result);
            allureStorageField.set(lifecycleStorage, allureStorage);
            lifecycleStorageField.set(lifecycle, lifecycleStorage);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // Имплементация для последнего завершенного шага

    public static void setLastStepStatusFailed() {
        dataTransfer.getLastStep().setStepStatusFailed();
        dataTransfer.getLastStep().editStatusIfNecessary();
    }

    public static void setLastStepName(String newStepName) {
        dataTransfer.getLastStep().setStepName(newStepName);
        dataTransfer.getLastStep().edit();
    }

    public static void deleteLastStepParameter(String parameterName) {
        dataTransfer.getLastStep().deleteStepParameter(parameterName);
        dataTransfer.getLastStep().edit();
    }

    public static void deleteLastStepAllParameters() {
        dataTransfer.getLastStep().deleteAllStepParameters();
        dataTransfer.getLastStep().edit();
    }

    public static void editLastStepParameterName(String oldParameterName, String newParameterName) {
        dataTransfer.getLastStep().editStepParameterName(oldParameterName, newParameterName);
        dataTransfer.getLastStep().edit();
    }

    public static void addLastStepParameter(String parameterName, String parameterValue) {
        dataTransfer.getLastStep().addStepParameter(parameterName, parameterValue);
        dataTransfer.getLastStep().edit();
    }

    // Имплементация для текущего шага

    public static void setCurrentStepStatusFailed() {
        dataTransfer.getCurrentStep().setStepStatusFailed();
    }

    public static void setCurrentStepName(String newStepName) {
        dataTransfer.getCurrentStep().setStepName(newStepName);
    }

    public static void deleteCurrentStepParameter(String parameterName) {
        dataTransfer.getCurrentStep().deleteStepParameter(parameterName);
    }

    public static void deleteCurrentStepAllParameters() {
        dataTransfer.getCurrentStep().deleteAllStepParameters();
    }

    public static void editCurrentStepParameterName(String oldParameterName, String newParameterName) {
        dataTransfer.getCurrentStep().editStepParameterName(oldParameterName, newParameterName);
    }

    public static void addCurrentStepParameter(String parameterName, String parameterValue) {
        dataTransfer.getCurrentStep().addStepParameter(parameterName, parameterValue);
    }

    // Имплементация для следующего шага
    public static void setNextStepName(String stepName) {
        DelayedEditableStep delayedEditableStep = DelayedEditableStep.getInstance();
        delayedEditableStep.setStepName(stepName);
    }

    public static void addNextStepParameter(String parameterName, String parameterValue) {
        DelayedEditableStep delayedEditableStep = DelayedEditableStep.getInstance();
        delayedEditableStep.addParameter(new Parameter().setName(parameterName).setValue(parameterValue));
    }

    public static void editNextStepParameterName(String oldParameterName, String newParameterName) {
        DelayedEditableStep delayedEditableStep = DelayedEditableStep.getInstance();
        delayedEditableStep.editParameterName(oldParameterName, newParameterName);
    }
}
