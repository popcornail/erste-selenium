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


//Test that validates that if we increase the income of our household the maximum loan that we can get is increasing as well
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
