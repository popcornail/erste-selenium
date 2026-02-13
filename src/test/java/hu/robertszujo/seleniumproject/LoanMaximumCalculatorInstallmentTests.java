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

@Listeners(TestListener.class)
public class LoanMaximumCalculatorInstallmentTests extends BaseTestClass {

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

    @Test(description = "Monthly installment does not exceed 50% for income below 500k")
    public void monthlyInstallment_IncomeBelowFiveHundredThousand_MaxFiftyPercent() {
        loadPageAndAcceptCookies(cookiePopup);

        int income = 400000;

        loanMaximumCalculatorPage.setAge(35);
        loanMaximumCalculatorPage.setPropertyValue(20000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(income);
        loanMaximumCalculatorPage.clickCalculate();

        Assertions.assertThat(loanMaximumCalculatorPage.hasCalculationResult())
                .as("Calculation should succeed")
                .isTrue();

        int box1Installment = loanMaximumCalculatorPage.getBox1MonthlyInstallment();
        int box2Installment = loanMaximumCalculatorPage.getBox2MonthlyInstallment();

        int maxAllowed = (int) (income * 0.50);

        reporter.info("Income < 500k Test");
        reporter.info("Income: " + income + " Ft");
        reporter.info("Max allowed (50%): " + maxAllowed + " Ft");
        reporter.info("Box 1 (10 year) installment: " + box1Installment + " Ft");
        reporter.info("Box 2 (20 year fix) installment: " + box2Installment + " Ft");

        Assertions.assertThat(box1Installment)
                .as("Box 1 monthly installment should not exceed 50% of income for income < 500k")
                .isLessThanOrEqualTo(maxAllowed);

        Assertions.assertThat(box2Installment)
                .as("Box 2 monthly installment should not exceed 50% of income for income < 500k")
                .isLessThanOrEqualTo(maxAllowed);

        reporter.pass("Monthly installments are within 50% limit for income < 500k");
    }

    @Test(description = "Monthly installment does not exceed 60% for income above or equal 500k")
    public void monthlyInstallment_IncomeAboveFiveHundredThousand_MaxSixtyPercent() {
        loadPageAndAcceptCookies(cookiePopup);

        int income = 600000;

        loanMaximumCalculatorPage.setAge(35);
        loanMaximumCalculatorPage.setPropertyValue(25000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(income);
        loanMaximumCalculatorPage.clickCalculate();

        Assertions.assertThat(loanMaximumCalculatorPage.hasCalculationResult())
                .as("Calculation should succeed")
                .isTrue();

        int box1Installment = loanMaximumCalculatorPage.getBox1MonthlyInstallment();
        int box2Installment = loanMaximumCalculatorPage.getBox2MonthlyInstallment();

        int maxAllowed = (int) (income * 0.60);

        reporter.info("Income ≥ 500k Test");
        reporter.info("Income: " + income + " Ft");
        reporter.info("Max allowed (60%): " + maxAllowed + " Ft");
        reporter.info("Box 1 (10 year) installment: " + box1Installment + " Ft");
        reporter.info("Box 2 (20 year fix) installment: " + box2Installment + " Ft");

        Assertions.assertThat(box1Installment)
                .as("Box 1 monthly installment should not exceed 60% of income for income ≥ 500k")
                .isLessThanOrEqualTo(maxAllowed);

        Assertions.assertThat(box2Installment)
                .as("Box 2 monthly installment should not exceed 60% of income for income ≥ 500k")
                .isLessThanOrEqualTo(maxAllowed);

        reporter.pass("Monthly installments are within 60% limit for income ≥ 500k");
    }
}
