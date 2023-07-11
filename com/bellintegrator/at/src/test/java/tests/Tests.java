package tests;

import allure.AllureEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steps.ModifiedSteps;

public class Tests {
    ModifiedSteps mSteps;

    @BeforeEach
    public void before() {
        mSteps = new ModifiedSteps();
    }

    @Test
    public void test1() {
        AllureEdit.startStep("bla bla {} {}", "bla 1", "bla 2");
        AllureEdit.setCurrentStepName("mla mla 1");
        AllureEdit.startStep("bla bla {} {}");
        AllureEdit.setNextStepName("Я изменился до старта");
        AllureEdit.addNextStepParameter("TEST", "TESTSTS");
        mSteps.doSmthManyTimes1("s");
        AllureEdit.addLastStepParameter("test", "testValue");
        AllureEdit.stopManuallyStep();
    }
}
