package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.internal.AllureStorage;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AllureEdit {
    private static AllureStepDataTransfer dataTransfer = AllureStepDataTransfer.getInstance();

    /**
     * @author Victor Avdonin
     * Основной метод замены информации в аллюр отчёте.
     * При помощи рефлексии получает доступ к хранилищу информации о шагах в аллюре,
     * Перезаписывает хранящийся StepResult на изменённый.
     * @param uuid - id заменяемого шага, получается с помощью AllureStepListener и передаётся через AllureStepDataTransfer
     * @param result - stepResult который записывается в отчёт вместо существующего
     */
    private static void replaceStepFromAllureReport(String uuid, StepResult result) {
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

    // Основной функционал

    public static void setStepStatusFailed(String uuid, StepResult result) {
        result.setStatus(Status.FAILED);
        replaceStepFromAllureReport(uuid, result);
    }

    public static void setStepName(String uuid, StepResult result, String newStepName) {
        result.setName(newStepName);
        replaceStepFromAllureReport(uuid, result);
    }

    public static void deleteStepParameter(String uuid, StepResult result, String parameterName) {
        List<Parameter> parameters = result.getParameters();
        parameters = parameters.stream().filter(parameter -> !parameter.getName().equals(parameterName)).collect(Collectors.toList());
        result.setParameters(parameters);
        replaceStepFromAllureReport(uuid, result);
    }

    public static void deleteStepAllParameters(String uuid, StepResult result) {
        List<Parameter> parameters = new ArrayList<>();
        result.setParameters(parameters);
        replaceStepFromAllureReport(uuid, result);
    }

    public static void editStepParameterName(String uuid, StepResult result, String oldParameterName, String newParameterName) {
        List<Parameter> parameters = result.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.getName().equals(oldParameterName)) {
                parameter.setName(newParameterName);
            }
        }
        result.setParameters(parameters);
        replaceStepFromAllureReport(uuid, result);
    }

    // Имплементация для последнего шага

    public static void setLastStepStatusFailed() {
        String uuid = dataTransfer.getLastStepUuid();
        StepResult result = dataTransfer.getLastStepResult();
        setStepStatusFailed(uuid, result);
    }

    public static void setLastStepName(String newStepName) {
        String uuid = dataTransfer.getLastStepUuid();
        StepResult result = dataTransfer.getLastStepResult();
        setStepName(uuid, result, newStepName);
    }

    public static void deleteLastStepParameter(String parameterName) {
        String uuid = dataTransfer.getLastStepUuid();
        StepResult result = dataTransfer.getLastStepResult();
        deleteStepParameter(uuid, result, parameterName);
    }
    public static void deleteLastStepAllParameters() {
        String uuid = dataTransfer.getLastStepUuid();
        StepResult result = dataTransfer.getLastStepResult();
        deleteStepAllParameters(uuid, result);
    }

    public static void editLastStepParameterName(String oldParameterName, String newParameterName) {
        String uuid = dataTransfer.getLastStepUuid();
        StepResult result = dataTransfer.getLastStepResult();
        editStepParameterName(uuid, result, oldParameterName, newParameterName);
    }

     // Имплементация для текущего шага

    public static void setCurrentStepStatusFailed() {
        String uuid = dataTransfer.getCurrentStepUuid();
        StepResult result = dataTransfer.getCurrentStepResult();
        setStepStatusFailed(uuid, result);
    }

    public static void setCurrentStepName(String newStepName) {
        String uuid = dataTransfer.getCurrentStepUuid();
        StepResult result = dataTransfer.getCurrentStepResult();
        setStepName(uuid, result, newStepName);
    }

    public static void deleteCurrentStepParameter(String parameterName) {
        String uuid = dataTransfer.getCurrentStepUuid();
        StepResult result = dataTransfer.getCurrentStepResult();
        deleteStepParameter(uuid, result, parameterName);
    }
    public static void deleteCurrentStepAllParameters() {
        String uuid = dataTransfer.getCurrentStepUuid();
        StepResult result = dataTransfer.getCurrentStepResult();
        deleteStepAllParameters(uuid, result);
    }

    public static void editCurrentStepParameterName(String oldParameterName, String newParameterName) {
        String uuid = dataTransfer.getCurrentStepUuid();
        StepResult result = dataTransfer.getCurrentStepResult();
        editStepParameterName(uuid, result, oldParameterName, newParameterName);
    }
}
