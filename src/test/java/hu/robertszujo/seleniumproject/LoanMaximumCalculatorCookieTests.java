package hu.robertszujo.seleniumproject;

import com.aventstack.extentreports.ExtentTest;
import hu.robertszujo.seleniumproject.constants.TestConstants;
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
public class LoanMaximumCalculatorCookieTests extends BaseTestClass {

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

    @Test(description = "Cookie popup should be displayed after page load")
    public void loadCalculatorPage_cookiePopupShouldBeDisplayed() {
        driver.get(TestConstants.MAXCALCULATOR_PAGE_URL);

        Assertions.assertThat(cookiePopup.isCookiePopupDisplayedAfterWaiting())
                .as("Cookie popup should have displayed after page load")
                .isTrue();
        reporter.pass("Cookie popup was displayed successfully");
    }

    @Test(description = "Cookie popup should disappear after accepting cookies")
    public void acceptCookies_CookiePopupShouldDisappear() {
        driver.get(TestConstants.MAXCALCULATOR_PAGE_URL);
        cookiePopup.waitForCookiePopupToBeDisplayed();

        cookiePopup.clickOnCookieAcceptButton();

        Assertions.assertThat(cookiePopup.hasCookiePopupDisappearedAfterWaiting())
                .as("Cookie popup should have disappeared")
                .isTrue();
        reporter.pass("Cookie popup has disappeared successfully");
    }

    @Test(description = "Calculator form should be displayed after page load & accepting cookies")
    public void loadPageAndAcceptCookies_CalculatorFormShouldBeDisplayed() {
        loadPageAndAcceptCookies(cookiePopup);

        Assertions.assertThat(loanMaximumCalculatorPage.isCalculatorFormDisplayedAfterWaiting())
                .as("Calculator form should be displayed")
                .isTrue();
        reporter.pass("Loan calculator page was displayed successfully");
    }
}
