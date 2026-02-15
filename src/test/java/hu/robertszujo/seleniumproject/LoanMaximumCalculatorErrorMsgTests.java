package hu.robertszujo.seleniumproject;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentTest;
import hu.robertszujo.seleniumproject.constants.TestContextConstants;
import hu.robertszujo.seleniumproject.pages.LoanMaximumCalculatorPage;
import hu.robertszujo.seleniumproject.pages.components.CookiePopup;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public class LoanMaximumCalculatorErrorMsgTests extends BaseTestClass {

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

    @Test(description = "Verify multiple errors are displayed simultaneously")
    public void multipleInvalidFields_AllErrorsShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(17);
        loanMaximumCalculatorPage.setPropertyValue(4000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(50000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.hasAnyError())
                .as("At least one error should be displayed")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.hasAgeError())
                .as("Age error should be displayed")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.hasPropertyValueError())
                .as("Property value error should be displayed")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.hasHouseholdIncomeError())
                .as("Household income error should be displayed")
                .isTrue();

        reporter.pass("Multiple validation errors displayed correctly");
    }
}