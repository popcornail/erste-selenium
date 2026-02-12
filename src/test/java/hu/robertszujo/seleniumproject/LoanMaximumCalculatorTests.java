package hu.robertszujo.seleniumproject;

import com.aventstack.extentreports.ExtentTest;
import hu.robertszujo.seleniumproject.constants.TestConstants;
import hu.robertszujo.seleniumproject.constants.TestContextConstants;
import hu.robertszujo.seleniumproject.pages.LoanMaximumCalculatorPage;
import org.assertj.core.api.Assertions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import hu.robertszujo.seleniumproject.pages.components.CookiePopup;


@Listeners(TestListener.class)
public class LoanMaximumCalculatorTests extends BaseTestClass {

    private ExtentTest reporter;


    // Page objects
    private LoanMaximumCalculatorPage loanMaximumCalculatorPage;
    private CookiePopup cookiePopup;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestContext context, ITestResult result) {
        reporter = SuiteWideStorage.testReport.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
        context.setAttribute(TestContextConstants.REPORTER, reporter);
        initializePageObjects();
    }

    public void initializePageObjects() {
        loanMaximumCalculatorPage = new LoanMaximumCalculatorPage(driver, reporter);
        cookiePopup = new CookiePopup(driver, reporter);
    }

    // *** Cookie Tests ***

    @Test(description = "Cookie popup should be displayed after page load")
    public void loadCalculatorPage_cookiePopupShouldBeDisplayed() {
        // Given + When
        driver.get(TestConstants.MAXCALCULATOR_PAGE_URL);

        // Then
        Assertions.assertThat(cookiePopup.isCookiePopupDisplayedAfterWaiting())
                .as("Cookie popup should have displayed after page load")
                .isTrue();
        reporter.pass("Cookie popup was displayed successfully");
    }

    @Test(description = "Cookie popup should disappear after accepting cookies")
    public void acceptCookies_CookiePopupShouldDisappear() {
        //Given
        driver.get(TestConstants.MAXCALCULATOR_PAGE_URL);
        cookiePopup.waitForCookiePopupToBeDisplayed();

        //When
        cookiePopup.clickOnCookieAcceptButton();

        //Then
        Assertions.assertThat(cookiePopup.hasCookiePopupDisappearedAfterWaiting())
                .as("Cookie popup should have disappeared")
                .isTrue();
        reporter.pass("Cookie popup has disappeared successfully");
    }

    @Test(description = "Calculator form should be displayed after page load & accepting cookies")
    public void loadPageAndAcceptCookies_CalculatorFormShouldBeDisplayed() {
        //Given + When
        loadPageAndAcceptCookies();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.isCalculatorFormDisplayedAfterWaiting())
                .as("Calculator form should be displayed")
                .isTrue();
        reporter.pass("Loan calculator page was displayed successfully");
    }

    // *** Positive Tests ***

    @Test(description = "Calculate maximum loan with minimum valid values")
    public void calculateWithMinimumValues_BothResultsShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(18);
        loanMaximumCalculatorPage.setPropertyValue(5000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(193000);
        loanMaximumCalculatorPage.setExistingLoan(0);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.areAllResultsDisplayed())
                .as("Both result boxes should be displayed")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getMaxLoanAmount1())
                .as("Box 1 max loan amount should be displayed")
                .isNotNull();

        Assertions.assertThat(loanMaximumCalculatorPage.getMaxLoanAmount2())
                .as("Box 2 max loan amount should be displayed")
                .isNotNull();

        reporter.pass("Calculation with minimum values successful - both offers displayed");
    }

    @Test(description = "Calculate maximum loan with multiple earners")
    public void calculateWithMultipleEarners_ResultsShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(35);
        loanMaximumCalculatorPage.setPropertyValue(50000000);
        loanMaximumCalculatorPage.selectMultipleEarners();
        loanMaximumCalculatorPage.setHouseholdIncome(500000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.isResultDisplayed())
                .as("Results should be displayed for multiple earners")
                .isTrue();
        reporter.pass("Calculation with multiple earners successful");
    }

    @Test(description = "Calculate loan with existing loans")
    public void calculateWithExistingLoans_ResultsShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(40);
        loanMaximumCalculatorPage.setPropertyValue(30000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(400000);
        loanMaximumCalculatorPage.setExistingLoan(50000);
        loanMaximumCalculatorPage.setCreditLimit(200000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.isResultDisplayed())
                .as("Results should be displayed with existing loans")
                .isTrue();
        reporter.pass("Calculation with existing loans successful");
    }

    @Test(description = "Calculate loan with all benefits enabled")
    public void calculateWithAllBenefits_ResultsShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
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

        //Then
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
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(20000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(300000);
        loanMaximumCalculatorPage.setExistingLoan(0);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.getTHMValue1())
                .as("Box 1 THM value should not be null")
                .isNotNull();

        Assertions.assertThat(loanMaximumCalculatorPage.getTHMValue2())
                .as("Box 2 THM value should not be null")
                .isNotNull();

        reporter.pass("Both THM values are displayed correctly");
    }

    // *** Negative Tests - Age Validation ***

    @Test(description = "Validate error message for age below minimum")
    public void ageBelow18_ErrorShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(17);
        loanMaximumCalculatorPage.setPropertyValue(20000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(200000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.hasAgeError())
                .as("Age error should be displayed for age < 18")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getAgeErrorMessage())
                .as("Age error message should contain '18. életévüket'")
                .contains("18. életévüket");

        Assertions.assertThat(loanMaximumCalculatorPage.isResultDisplayed())
                .as("Results should NOT be displayed with invalid age")
                .isFalse();

        reporter.pass("Age validation works correctly for age < 18");
    }

    @Test(description = "Age above 65 should show callback button instead of calculate")
    public void ageAbove65_CallbackButtonShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(71);
        loanMaximumCalculatorPage.selectSingleEarner();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.isCallbackButtonVisible())
                .as("Callback button should be visible for age > 65")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.isCalculateButtonVisible())
                .as("Calculate button should NOT be visible for age > 65")
                .isFalse();

        reporter.pass("Callback button displayed correctly for age > 65");
    }


    // *** Negative Tests - Property Value Validation ***

    @Test(description = "Validate error for property value below minimum")
    public void propertyValueBelow5Million_ErrorShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(4999999);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(200000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.hasPropertyValueError())
                .as("Property value error should be displayed for value < 5M")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getPropertyValueErrorMessage())
                .as("Error message should contain 'Minimum 5 millió'")
                .contains("Minimum 5 millió");

        reporter.pass("Property value validation works correctly");
    }


    @Test(description = "Verify property value boundary - 5 million should be valid")
    public void propertyValue5Million_ShouldBeValid() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(5000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(200000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.hasPropertyValueError())
                .as("Property value error should NOT be displayed for value = 5M")
                .isFalse();

        reporter.pass("Property value boundary validation works correctly");
    }

    // *** Negative Tests - Household Income Validation ***

    @Test(description = "Validate error for household income below minimum")
    public void householdIncomeBelow100k_ErrorShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(30);
        loanMaximumCalculatorPage.setPropertyValue(20000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(99999);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.hasHouseholdIncomeError())
                .as("Household income error should be displayed for income < 100k")
                .isTrue();

        Assertions.assertThat(loanMaximumCalculatorPage.getHouseholdIncomeErrorMessage())
                .as("Error message should contain 'jövedelem'")
                .contains("jövedelem");

        reporter.pass("Household income validation works correctly");
    }

    @Test(description = "Verify multiple errors are displayed simultaneously")
    public void multipleInvalidFields_AllErrorsShouldBeDisplayed() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.setAge(17);
        loanMaximumCalculatorPage.setPropertyValue(4000000);
        loanMaximumCalculatorPage.selectSingleEarner();
        loanMaximumCalculatorPage.setHouseholdIncome(50000);
        loanMaximumCalculatorPage.clickCalculate();
        loanMaximumCalculatorPage.waitForResultsToLoad();

        //Then
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

    // *** Checkbox Tests ***

    @Test(description = "Regular income checkbox enables amount field")
    public void checkRegularIncomeCheckbox_AmountFieldShouldBeEnabled() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.selectRegularIncomeCheckbox();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.isRegularIncomeChecked())
                .as("Regular income checkbox should be checked")
                .isTrue();

        reporter.pass("Regular income checkbox works correctly");
    }

    @Test(description = "Baby expecting checkbox can be selected")
    public void selectBabyExpectingCheckbox_ShouldBeChecked() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.selectBabyExpectingCheckbox();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.isBabyExpectingChecked())
                .as("Baby expecting checkbox should be checked")
                .isTrue();

        reporter.pass("Baby expecting checkbox works correctly");
    }

    @Test(description = "Loan protection insurance checkbox can be selected")
    public void enableLoanProtectionInsurance_ShouldBeChecked() {
        //Given
        loadPageAndAcceptCookies();

        //When
        loanMaximumCalculatorPage.selectLoanProtectionInsuranceCheckbox();

        //Then
        Assertions.assertThat(loanMaximumCalculatorPage.isLoanProtectionInsuranceChecked())
                .as("Loan protection insurance should be checked")
                .isTrue();

        reporter.pass("Loan protection insurance checkbox works correctly");
    }

    // *** Helper methods ***

    private void loadPageAndAcceptCookies() {
        driver.get(TestConstants.MAXCALCULATOR_PAGE_URL);
        cookiePopup.waitForCookiePopupToBeDisplayed();
        cookiePopup.clickOnCookieAcceptButton();
        cookiePopup.waitForCookiePopupToDisappear();
    }
}
