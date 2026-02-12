package hu.robertszujo.seleniumproject.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.aventstack.extentreports.ExtentTest;
import hu.robertszujo.seleniumproject.utils.ElementActions;
import org.openqa.selenium.support.FindBy;


public class LoanMaximumCalculatorPage extends BasePageObject {

    // === LOCATORS ===

    // Basic data
    @FindBy(id = "ingatlan_erteke")
    private WebElement propertyValueInput;
    @FindBy(id = "ingatlan_erteke_error")
    private WebElement PropertyValueError;

    @FindBy(id = "meletkor")
    private WebElement ageInput;
    @FindBy(id = "eletkor_error")
    private WebElement ageError;

    // Household
    @FindBy(id = "egyedul")
    private WebElement singleEarnerButton;

    @FindBy(id = "tobben")
    private WebElement multipleEarnersButton;

    @FindBy(id = "mjovedelem")
    private WebElement householdIncomeInput;
    @FindBy(id = "mjovedelem_error")
    private WebElement HouseholdIncomeError;

    // Existing loans
    @FindBy(id = "meglevo_torleszto")
    private WebElement existingLoanInstallmentInput;
    @FindBy(id = "meglevo_torleszto_error")
    private WebElement ExistingLoanInstallmentError;

    @FindBy(id = "folyoszamla")
    private WebElement creditLimitInput;

    // Benefits / Discounts
    @FindBy(id = "kedvezmeny_jovairasm")
    private WebElement regularIncomeCheckbox;

    @FindBy(id = "jovairasm")
    private WebElement regularIncomeAmountInput;

    @FindBy(id = "kedvezmeny_babavarom")
    private WebElement babyExpectingCheckbox;

    @FindBy(id = "kedvezmeny_biztositasm")
    private WebElement loanProtectionInsuranceCheckbox;

    // Button
    @FindBy(css = ".btn.btn-orange.mennyit_kaphatok")
    private WebElement calculateButton;

    //Max Loan
    @FindBy(id = "box_1_max_desktop")
    private WebElement maxLoanAmount1;
    @FindBy(id = "box_2_max_desktop")
    private WebElement maxLoanAmount2;
    // THM
    @FindBy(id = "box_1_thm")
    private WebElement thmValue1;
    @FindBy(id = "box_2_thm")
    private WebElement thmValue2;


    public LoanMaximumCalculatorPage(WebDriver driver, ExtentTest reporter) {
        super(driver, reporter);
    }

    private void waitAndSetInput(WebElement element, String value, String fieldName) {
        ElementActions.waitForElementToBeDisplayed(element, driver);
        reporter.info("Waiting for " + fieldName + " field to be displayed");
        element.clear();
        element.sendKeys(value);
        reporter.info(fieldName + " set to: " + value);
    }

    private void waitAndClickButton(WebElement element, String buttonName) {
        ElementActions.waitForElementToBeDisplayed(element, driver);
        reporter.info("Waiting for " + buttonName + " button to be displayed");
        element.click();
        reporter.info("Selected: " + buttonName);
    }

    public void setPropertyValue(int value) {
        waitAndSetInput(propertyValueInput, String.valueOf(value), "Property value");
    }

    public void setAge(int age) {
        waitAndSetInput(ageInput, String.valueOf(age), "Age");
    }

    public void selectSingleEarner() {
        waitAndClickButton(singleEarnerButton, "Single earner");
    }

    public void selectMultipleEarners() {
        waitAndClickButton(multipleEarnersButton, "Multiple earners");
    }

    public void setHouseholdIncome(int income) {
        waitAndSetInput(householdIncomeInput, String.valueOf(income), "Household income");
    }

    public void setExistingLoan(int loan){
        waitAndSetInput(existingLoanInstallmentInput, String.valueOf(loan),"Existing loan");
    }

    public void setCreditLimit(int limit){
        waitAndSetInput(creditLimitInput, String.valueOf(limit), "Credit card limit");
    }

    public void checkRegularIncomeCheckbox() {
        waitAndClickButton(regularIncomeCheckbox, "Regular income checkbox");
    }

    public void setRegularIncome(int regularIncome){
        waitAndSetInput(regularIncomeAmountInput, String.valueOf(regularIncome), "Regular income amount");
    }

    public void selectBabyExpectingCheckbox(){
        waitAndClickButton(babyExpectingCheckbox, "Baby expecting checkbox");
    }

    private void selectCalculateButton(){
        waitAndClickButton(calculateButton, "Button for calculating results");
    }
}