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
public class LoanMaximumCalculatorDebtTests extends BaseTestClass {

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

    @Test(description = "Increasing existing loan installment decreases maximum loan amount")
    public void existingLoanInstallmentIncrease_DecreasesLoanAmount() {
        loadPageAndAcceptCookies(cookiePopup);

        int age = 18;
        int propertyValue = 43200000;
        int income = 1000000;
        int existingLoanInstallmentLow = 350000;

        loanMaximumCalculatorPage.setAge(age);
        loanMaximumCalculatorPage.setPropertyValue(propertyValue);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(income);
        loanMaximumCalculatorPage.setExistingLoan(existingLoanInstallmentLow);
        loanMaximumCalculatorPage.clickCalculate();

        Assertions.assertThat(loanMaximumCalculatorPage.hasCalculationResult())
                .as("First calculation should succeed")
                .isTrue();

        int loanAmountHigh1 = loanMaximumCalculatorPage.getBox1LoanAmount();
        int loanAmountHigh2 = loanMaximumCalculatorPage.getBox2LoanAmount();

        reporter.info("Existing loan installment: " + existingLoanInstallmentLow);
        reporter.info("Box 1 loan amount: " + loanAmountHigh1);
        reporter.info("Box 2 loan amount: " + loanAmountHigh2);

        driver.navigate().refresh();

        try {
            cookiePopup.waitForCookiePopupToBeDisplayed();
            cookiePopup.clickOnCookieAcceptButton();
        } catch (TimeoutException e) {
            reporter.info("Cookie popup not displayed after refresh");
        }

        int existingLoanInstallmentHigh = 400000;

        loanMaximumCalculatorPage.setAge(age);
        loanMaximumCalculatorPage.setPropertyValue(propertyValue);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(income);
        loanMaximumCalculatorPage.setExistingLoan(existingLoanInstallmentHigh);
        loanMaximumCalculatorPage.clickCalculate();

        Assertions.assertThat(loanMaximumCalculatorPage.hasCalculationResult())
                .as("Second calculation should succeed")
                .isTrue();

        int loanAmountLow1 = loanMaximumCalculatorPage.getBox1LoanAmount();
        int loanAmountLow2 = loanMaximumCalculatorPage.getBox2LoanAmount();

        reporter.info("Existing loan installment: " + existingLoanInstallmentHigh);
        reporter.info("Box 1 loan amount: " + loanAmountLow1);
        reporter.info("Box 2 loan amount: " + loanAmountLow2);

        Assertions.assertThat(loanAmountLow1)
                .as("Box 1 loan amount should decrease with higher existing loan installment")
                .isLessThan(loanAmountHigh1);

        Assertions.assertThat(loanAmountLow2)
                .as("Box 2 loan amount should decrease with higher existing loan installment")
                .isLessThan(loanAmountHigh2);

        reporter.pass("Existing loan installment increase resulted in loan amount decrease");
    }
}
