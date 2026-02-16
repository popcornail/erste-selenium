package hu.robertszujo.seleniumproject;

import com.aventstack.extentreports.ExtentTest;
import hu.robertszujo.seleniumproject.constants.TestContextConstants;
import hu.robertszujo.seleniumproject.pages.LoanMaximumCalculatorPage;
import hu.robertszujo.seleniumproject.pages.components.CookiePopup;
import org.assertj.core.api.Assertions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

//Tests for verifying the working of the age field
@Listeners(TestListener.class)
public class LoanMaximumCalculatorAgeValidationTests extends BaseTestClass {

    private ExtentTest reporter;
    private LoanMaximumCalculatorPage loanMaximumCalculatorPage;
    private CookiePopup cookiePopup;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestContext context, ITestResult result) {
        reporter = SuiteWideStorage.testReport.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription()
        );
        context.setAttribute(TestContextConstants.REPORTER, reporter);
        loanMaximumCalculatorPage = new LoanMaximumCalculatorPage(driver, reporter);
        cookiePopup = new CookiePopup(driver, reporter);
    }

    @Test(description = "Validate error message for age below minimum")
    public void ageBelow18_ErrorShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(17);
        loanMaximumCalculatorPage.setPropertyValue(20000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(200000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.hasAgeError())
                .as("Age error should be displayed for age < 18")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getAgeErrorMessage())
                .as("Age error message should contain '18. életévüket'")
                .contains("18. életévüket");

        Assertions.assertThat(loanMaximumCalculatorPage.isResultDisplayed())
                .as("Results should NOT be displayed with invalid age")
                .isFalse();

        reporter.pass("Age validation works correctly for age < 18");
    }

    @Test(description = "Age above 65 should show callback button instead of calculate")
    public void ageAbove65_CallbackButtonShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(71);
        loanMaximumCalculatorPage.selectSingleEarner();

        Assertions.assertThat(loanMaximumCalculatorPage.isCallbackButtonVisible())
                .as("Callback button should be visible for age > 65")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.isCalculateButtonVisible())
                .as("Calculate button should NOT be visible for age > 65")
                .isFalse();

        reporter.pass("Callback button displayed correctly for age > 65");
    }
}
