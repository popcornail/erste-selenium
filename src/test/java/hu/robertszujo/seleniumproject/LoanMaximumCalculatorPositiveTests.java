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
public class LoanMaximumCalculatorPositiveTests extends BaseTestClass {

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

    @Test(description = "Calculate maximum loan with minimum valid values")
    public void calculateWithMinimumValues_BothResultsShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(18);
        loanMaximumCalculatorPage.setPropertyValue(5000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(193000);
        loanMaximumCalculatorPage.setExistingLoan(0);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.areAllResultsDisplayed())
                .as("Both result boxes should be displayed")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getBox1LoanAmount())
                .as("Box 1 max loan amount should be displayed")
                .isGreaterThan(0);

        Assertions.assertThat(loanMaximumCalculatorPage.getBox2LoanAmount())
                .as("Box 2 max loan amount should be displayed")
                .isGreaterThan(0);

        reporter.pass("Calculation with minimum values successful - both offers displayed");
    }

    @Test(description = "Calculate maximum loan with multiple earners")
    public void calculateWithMultipleEarners_ResultsShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(35);
        loanMaximumCalculatorPage.setPropertyValue(5000000);
        loanMaximumCalculatorPage.selectMultipleEarners();
        loanMaximumCalculatorPage.setHouseholdIncome(500000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.isResultDisplayed())
                .as("Results should be displayed for multiple earners")
                .isTrue();
        reporter.pass("Calculation with multiple earners successful");
    }

    @Test(description = "Calculate loan with existing loans")
    public void calculateWithExistingLoans_ResultsShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(40);
        loanMaximumCalculatorPage.setPropertyValue(30000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(400000);
        loanMaximumCalculatorPage.setExistingLoan(50000);
        loanMaximumCalculatorPage.setCreditLimit(200000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.isResultDisplayed())
                .as("Results should be displayed with existing loans")
                .isTrue();
        reporter.pass("Calculation with existing loans successful");
    }

    @Test(description = "Calculate loan with all benefits enabled")
    public void calculateWithAllBenefits_ResultsShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(40000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(350000);

        loanMaximumCalculatorPage.selectRegularIncomeCheckbox();
        loanMaximumCalculatorPage.setRegularIncome(100000);
        loanMaximumCalculatorPage.selectBabyExpectingCheckbox();
        loanMaximumCalculatorPage.selectLoanProtectionInsuranceCheckbox();

        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.isResultDisplayed())
                .as("Results should be displayed with all benefits")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.isRegularIncomeChecked())
                .as("Regular income checkbox should be checked")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.isBabyExpectingChecked())
                .as("Baby expecting checkbox should be checked")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.isLoanProtectionInsuranceChecked())
                .as("Loan protection insurance should be checked")
                .isTrue();

        reporter.pass("Calculation with all benefits successful");
    }

    @Test(description = "Verify both result boxes contain THM values")
    public void calculateLoan_BothTHMValuesShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(20000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(300000);
        loanMaximumCalculatorPage.setExistingLoan(0);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        Assertions.assertThat(loanMaximumCalculatorPage.getBox1THM())
                .as("Box 1 THM value should not be null")
                .isGreaterThan(0);

        Assertions.assertThat(loanMaximumCalculatorPage.getBox2THM())
                .as("Box 2 THM value should not be null")
                .isGreaterThan(0);

        reporter.pass("Both THM values are displayed correctly");
    }
}
