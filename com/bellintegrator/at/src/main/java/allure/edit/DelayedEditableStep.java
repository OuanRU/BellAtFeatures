package allure.edit;

import io.qameta.allure.model.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelayedEditableStep {
    private static DelayedEditableStep instance;
    private static String stepName;
    private static Map<String, String> editedParameters;
    private static List<Parameter> addedParameters;

    private DelayedEditableStep() {
    }

    public static DelayedEditableStep getInstance() {
        if (instance == null) {
            instance = new DelayedEditableStep();
            editedParameters = new HashMap<>();
            addedParameters = new ArrayList<>();
            stepName = null;
        }
        return instance;
    }

    protected String getStepName() {
        return stepName;
    }

    protected void setStepName(String stepName) {
        DelayedEditableStep.stepName = stepName;
    }

    protected Map<String, String> getEditedParameters() {
        return editedParameters;
    }

    protected void editParameterName(String oldParamName, String newParamName) {
        editedParameters.put(oldParamName, newParamName);
    }

    protected List<Parameter> getAddedParameters() {
        return addedParameters;
    }

    protected void addParameter(Parameter addParameter) {
        addedParameters.add(addParameter);
    }

    protected void clearDelayedStep() {
        instance = null;
    }

    protected boolean isDelayedEdit() {
        return instance != null;
    }

    protected boolean isStepNameEdited() {
        return stepName != null;
    }

    protected boolean isStepParametersEdited() {
        return editedParameters.size() > 0;
    }

    protected boolean isStepParametersAdded() {
        return addedParameters.size() > 0;
    }
}
