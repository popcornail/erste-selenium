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

//Tests that are validating the proper working of the checkboxes
@Listeners(TestListener.class)
public class LoanMaximumCalculatorCheckboxTests extends BaseTestClass {

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

    @Test(description = "Regular income checkbox enables amount field")
    public void checkRegularIncomeCheckbox_AmountFieldShouldBeEnabled() {
        loadPageAndAcceptCookies(cookiePopup);

        loanMaximumCalculatorPage.selectRegularIncomeCheckbox();

        Assertions.assertThat(loanMaximumCalculatorPage.isRegularIncomeChecked())
                .as("Regular income checkbox should be checked")
                .isTrue();

        reporter.pass("Regular income checkbox works correctly");
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
}
