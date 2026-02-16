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

//Negatives test that checks that the error messages are present when we give wrong  data
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
    @Test(description = "Validate error for household income below minimum for single earners")
    public void singleEarnerHouseholdIncomeBelow193k_ErrorShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(20000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(192999);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.hasHouseholdIncomeError())
                .as("Household income error should be displayed for income < 193k")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getHouseholdIncomeErrorMessage())
                .as("Error message should contain 'jövedelem'")
                .contains("jövedelem");

        reporter.pass("Household income validation works correctly");
    }


    @Test(description = "Validate error for household income below minimum for multiple earners")
    public void multipleEarnersHouseholdIncomeBelow289k_ErrorShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(20000000);
        loanMaximumCalculatorPage.selectMultipleEarners();
        loanMaximumCalculatorPage.setHouseholdIncome(289999);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.hasHouseholdIncomeError())
                .as("Household income error should be displayed for income < 290k")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getHouseholdIncomeErrorMessage())
                .as("Error message should contain 'jövedelem'")
                .contains("jövedelem");

        reporter.pass("Household income validation works correctly");
    }

    @Test(description = "Validate error for property value below minimum")
    public void propertyValueBelow5Million_ErrorShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(4999999);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(200000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.hasPropertyValueError())
                .as("Property value error should be displayed for value < 5M")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getPropertyValueErrorMessage())
                .as("Error message should contain 'Minimum 5 millió'")
                .contains("Minimum 5 millió");

        reporter.pass("Property value validation works correctly");
    }

}