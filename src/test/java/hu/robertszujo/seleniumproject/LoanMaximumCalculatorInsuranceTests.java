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
public class LoanMaximumCalculatorInsuranceTests extends BaseTestClass {

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

    @Test(description = "Selecting loan protection insurance decreases THM")
    public void loanProtectionInsurance_DecreasesAPR() {
        loadPageAndAcceptCookies(cookiePopup);

        int age = 35;
        int propertyValue = 30000000;
        int income = 1000000;

        loanMaximumCalculatorPage.setAge(age);
        loanMaximumCalculatorPage.setPropertyValue(propertyValue);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(income);
        loanMaximumCalculatorPage.clickCalculate();

        Assertions.assertThat(loanMaximumCalculatorPage.hasCalculationResult())
                .as("First calculation should succeed")
                .isTrue();

        double thmWithoutInsurance1 = loanMaximumCalculatorPage.getBox1THM();
        double thmWithoutInsurance2 = loanMaximumCalculatorPage.getBox2THM();

        reporter.info("Without loan protection insurance:");
        reporter.info("Box 1 THM: " + thmWithoutInsurance1);
        reporter.info("Box 2 THM: " + thmWithoutInsurance2);

        driver.navigate().refresh();

        try {
            cookiePopup.waitForCookiePopupToBeDisplayed();
            cookiePopup.clickOnCookieAcceptButton();
        } catch (TimeoutException e) {
            reporter.info("Cookie popup not displayed after refresh");
        }

        loanMaximumCalculatorPage.setAge(age);
        loanMaximumCalculatorPage.setPropertyValue(propertyValue);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(income);
        loanMaximumCalculatorPage.selectLoanProtectionInsuranceCheckbox();
        loanMaximumCalculatorPage.clickCalculate();

        Assertions.assertThat(loanMaximumCalculatorPage.hasCalculationResult())
                .as("Second calculation should succeed")
                .isTrue();

        double thmWithInsurance1 = loanMaximumCalculatorPage.getBox1THM();
        double thmWithInsurance2 = loanMaximumCalculatorPage.getBox2THM();

        reporter.info("With loan protection insurance:");
        reporter.info("Box 1 THM: " + thmWithInsurance1);
        reporter.info("Box 2 THM: " + thmWithInsurance2);

        Assertions.assertThat(thmWithInsurance1)
                .as("Box 1 THM should decrease with loan protection insurance")
                .isLessThan(thmWithoutInsurance1);

        Assertions.assertThat(thmWithInsurance2)
                .as("Box 2 THM should decrease with loan protection insurance")
                .isLessThan(thmWithoutInsurance2);

        reporter.pass("Loan protection insurance resulted in THM decrease");
    }
}
