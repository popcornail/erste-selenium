package hu.robertszujo.seleniumproject;

import com.aventstack.extentreports.ExtentTest;
import hu.robertszujo.seleniumproject.constants.TestContextConstants;
import hu.robertszujo.seleniumproject.pages.LoanMaximumCalculatorPage;
import hu.robertszujo.seleniumproject.pages.components.CookiePopup;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.TimeoutException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
public class LoanMaximumCalculatorIncomeValidationTests extends BaseTestClass {

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

    @Test(description = "Increasing household income increases maximum loan amount")
    public void householdIncomeIncrease_IncreasesLoanAmount() {
        loadPageAndAcceptCookies(cookiePopup);

        int age = 35;
        int propertyValue = 50000000;
        int incomeLow = 500000;

        loanMaximumCalculatorPage.setAge(age);
        loanMaximumCalculatorPage.setPropertyValue(propertyValue);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(incomeLow);
        loanMaximumCalculatorPage.clickCalculate();

        Assertions.assertThat(loanMaximumCalculatorPage.hasCalculationResult())
                .as("First calculation should succeed")
                .isTrue();

        int loanAmountLow1 = loanMaximumCalculatorPage.getBox1LoanAmount();
        int loanAmountLow2 = loanMaximumCalculatorPage.getBox2LoanAmount();

        reporter.info("Household income: " + incomeLow);
        reporter.info("Box 1 loan amount: " + loanAmountLow1);
        reporter.info("Box 2 loan amount: " + loanAmountLow2);

        driver.navigate().refresh();

        try {
            cookiePopup.waitForCookiePopupToBeDisplayed();
            cookiePopup.clickOnCookieAcceptButton();
        } catch (TimeoutException e) {
            reporter.info("Cookie popup not displayed after refresh");
        }

        int incomeHigh = 1500000;

        loanMaximumCalculatorPage.setAge(age);
        loanMaximumCalculatorPage.setPropertyValue(propertyValue);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(incomeHigh);
        loanMaximumCalculatorPage.clickCalculate();

        Assertions.assertThat(loanMaximumCalculatorPage.hasCalculationResult())
                .as("Second calculation should succeed")
                .isTrue();

        int loanAmountHigh1 = loanMaximumCalculatorPage.getBox1LoanAmount();
        int loanAmountHigh2 = loanMaximumCalculatorPage.getBox2LoanAmount();

        reporter.info("Household income: " + incomeHigh);
        reporter.info("Box 1 loan amount: " + loanAmountHigh1);
        reporter.info("Box 2 loan amount: " + loanAmountHigh2);

        Assertions.assertThat(loanAmountHigh1)
                .as("Box 1 loan amount should increase with higher household income")
                .isGreaterThan(loanAmountLow1);

        Assertions.assertThat(loanAmountHigh2)
                .as("Box 2 loan amount should increase with higher household income")
                .isGreaterThan(loanAmountLow2);

        reporter.pass("Household income increase resulted in loan amount increase");
    }
}
