package steps;

import allure.edit.AllureEdit;
import io.qameta.allure.Step;

public class ModifiedSteps {

    @Step("Сделать чего-нибудь")
    public void doSmth() {
        AllureEdit.setCurrentStepStatusFailed();
    }

    @Step("Сделать чего-нибудь")
    public void doSmthWithParams(String params) {

    }

    @Step("Сделать чего-нибудь много раз")
    public void doSmthManyTimes(String s) {
        for (int i = 1; i < 3; i++) {
            doSmth();
        }
        AllureEdit.deleteCurrentStepAllParameters();
        AllureEdit.addCurrentStepParameter("test", "testValue");
    }

    @Step("Сделать чего-нибудь много раз 11")
    public void doSmthManyTimes1(String s) {
        AllureEdit.setNextStepName("И я изменился до старта");
        doSmthManyTimes(s);
    }

    public void rest() {
//        RequestSpecification spec = new RequestSpecBuilder()
//                .setContentType(ContentType.ANY)
//                .setBaseUri("uri")
//                .addFilter(new RequestLoggingFilter())
    }
}
