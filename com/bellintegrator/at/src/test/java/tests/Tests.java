package tests;

import allure.edit.AllureEdit;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steps.ModifiedSteps;

import static allure.edit.AllureEdit.startStep;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Tests {
    ModifiedSteps mSteps;

    @BeforeEach
    public void before() {
        mSteps = new ModifiedSteps();
    }

    @Test
    public void test1() {
        startStep("bla bla {} {}", "bla 1", "bla 2");
        AllureEdit.setCurrentStepName("mla mla 1");
        startStep("bla bla {} {}");
        AllureEdit.setNextStepName("Я изменился до старта");
        AllureEdit.addNextStepParameter("TEST", "TESTSTS");
        mSteps.doSmthManyTimes1("s");
        AllureEdit.addLastStepParameter("test", "testValue");
        AllureEdit.stopManuallyStep();
    }

    @Test
    public void testAssertj() {
        String s = "s";
        String ss = "s";
        String sss = "s";
        mSteps.assertTest(s, "ss");
        assertThat(s).isEqualTo("s");
        assertThat(ss).isEqualTo("ss");
        assertThat(sss).isEqualTo("sss");


//        AllureEdit.makeStepAssetion("test", );

        SoftAssertions soft = new SoftAssertions();

        soft.assertThat(sss).isEqualTo("sss");
        soft.assertThat(ss).isEqualTo("ss");
        soft.assertThat(s).isEqualTo("s");

        soft.assertAll();
    }
}
