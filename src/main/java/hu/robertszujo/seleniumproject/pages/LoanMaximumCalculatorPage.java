package hu.robertszujo.seleniumproject.pages;

import org.openqa.selenium.*;
import com.aventstack.extentreports.ExtentTest;
import hu.robertszujo.seleniumproject.utils.ElementActions;
import org.openqa.selenium.support.FindBy;


public class LoanMaximumCalculatorPage extends BasePageObject {

    //LOCATORS

    @FindBy(css = "div[class='content_hitelmaximum']")
    private WebElement calculatorForm;

    // Basic data

    //Value of property
    @FindBy(id = "ingatlan_erteke")
    private WebElement propertyValueInput;
    @FindBy(id = "ingatlan_erteke_error")
    private WebElement propertyValueError;

    //Field for providing age
    @FindBy(id = "meletkor")
    private WebElement ageInput;
    @FindBy(id = "eletkor_error")
    private WebElement ageError;

    // Household

    //Button for choosing that you are a single earner
    @FindBy(id = "egyedul")
    private WebElement singleEarnerButton;

    //Button for choosing that there are more earners in the household
    @FindBy(id = "tobben")
    private WebElement multipleEarnersButton;

    //Field for household income
    @FindBy(id = "mjovedelem")
    private WebElement householdIncomeInput;
    @FindBy(id = "mjovedelem_error")
    private WebElement householdIncomeError;

    // Existing loans
    @FindBy(id = "meglevo_torleszto")
    private WebElement existingLoanInstallmentInput;
    @FindBy(id = "meglevo_torleszto_error")
    private WebElement existingLoanInstallmentError;

    //Credit card limit
    @FindBy(id = "folyoszamla")
    private WebElement creditLimitInput;

    // Benefits / Discounts

    //Button for choosing that you have a monthly income to your Erste card
    @FindBy(id = "kedvezmeny_jovairasm")
    private WebElement regularIncomeCheckbox;

    //Field for specifying how much is your monthly income for your Erste card
    @FindBy(id = "jovairasm")
    private WebElement regularIncomeAmountInput;

    //Button to choose that you want a "Baby Expecting" loan as well
    @FindBy(id = "kedvezmeny_babavarom")
    private WebElement babyExpectingCheckbox;

    //Button for choosing that you want loan protection
    @FindBy(id = "kedvezmeny_biztositasm")
    private WebElement loanProtectionInsuranceCheckbox;

    //The calculate button
    @FindBy(css = ".btn.btn-orange.mennyit_kaphatok")
    private WebElement calculateButton;
    @FindBy(id = "nem_tudunk_kalkulalni")
    private WebElement callbackButton;

    //Max Loan that you can get
    @FindBy(id = "box_1_max_desktop")
    private WebElement maxLoanAmount1;
    @FindBy(id = "box_2_max_desktop")
    private WebElement maxLoanAmount2;

    // THM
    @FindBy(id = "box_1_thm")
    private WebElement thmValue1;
    @FindBy(id = "box_2_thm")
    private WebElement thmValue2;

    //Monthly installment
    @FindBy(id = "box_1_torleszto")
    private WebElement monthlyInstallment1;
    @FindBy(id="box_2_torleszto")
    private WebElement monthlyInstallment2;

    //Page Object
    public LoanMaximumCalculatorPage(WebDriver driver, ExtentTest reporter) {
        super(driver, reporter);
    }

    //Function that waits for an element to be present in the DOM and fills the field with the given input
    private void waitAndSetInput(WebElement element, String value, String fieldName) {
        ElementActions.waitForElementToBeDisplayed(element, driver);
        reporter.info("Waiting for " + fieldName + " field to be displayed");
        element.clear();
        element.sendKeys(value);
        reporter.info(fieldName + " set to: " + value);
    }

    //Function that waits for an element to be present in the DOM and clicks on the element
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

    //Function that checks the regular income checkbox first and checks that the corresponding field to appeared
    public void selectRegularIncomeCheckbox() {
        ElementActions.waitForElementToBeDisplayed(regularIncomeCheckbox, driver);
        reporter.info("Clicking regular income checkbox");

        if (!isRegularIncomeChecked()) {
            regularIncomeCheckbox.click();
            try {
                ElementActions.waitForElementToBeDisplayed(regularIncomeAmountInput, driver);
                reporter.info("Regular income amount field is now visible");
            } catch (TimeoutException e) {
                reporter.warning("Regular income amount field did not appear");
            }
        }

        reporter.info("Regular income checkbox checked: " + isRegularIncomeChecked());
    }

    public void setRegularIncome(int regularIncome) {
        try {
            regularIncomeAmountInput.clear();
            regularIncomeAmountInput.sendKeys(String.valueOf(regularIncome));
            reporter.info("Regular income amount set to: " + regularIncome);
        } catch (Exception e) {
            reporter.fail("Regular income amount field is not visible! Checkbox must be checked first.");
            throw e;
        }
    }

    public void selectBabyExpectingCheckbox(){
        waitAndClickButton(babyExpectingCheckbox, "Baby expecting checkbox");
    }

    public void selectLoanProtectionInsuranceCheckbox(){
        waitAndClickButton(loanProtectionInsuranceCheckbox, "Baby expecting checkbox");
    }

    public boolean isCalculateButtonVisible() {
        try {
            return calculateButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    //Function that clicks on the calculate button but after it checks if the callback button is in its place instead
    public void clickCalculate() {
        if (isCalculateButtonVisible()) {
            ElementActions.waitForElementToBeDisplayed(calculateButton, driver);
            reporter.info("Clicking on the calculate button");
            calculateButton.click();
            reporter.info("Calculate button clicked");
        } else if (isCallbackButtonVisible()) {
            reporter.warning("Cannot calculate - Callback button is shown (age > 65)");
        } else {
            reporter.fail("No button found!");
            throw new NoSuchElementException("Calculate or callback button not found");
        }
    }

    // Error checking
    public boolean hasAgeError() {
        try {
            return ageError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public void waitForCalculatorFormToBeDisplayed() {
        reporter.info("Waiting for calculator form to be displayed");
        ElementActions.waitForElementToBeDisplayed(calculatorForm, driver);
    }
    public boolean isCalculatorFormDisplayedAfterWaiting() {
        try {
            waitForCalculatorFormToBeDisplayed();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getAgeErrorMessage() {
        try {
            ElementActions.waitForElementToBeDisplayed(ageError, driver);
            return ageError.getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasPropertyValueError() {
        try {
            return propertyValueError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPropertyValueErrorMessage() {
        try {
            ElementActions.waitForElementToBeDisplayed(propertyValueError, driver);
            return propertyValueError.getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasHouseholdIncomeError() {
        try {
            return householdIncomeError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getHouseholdIncomeErrorMessage() {
        try {
            ElementActions.waitForElementToBeDisplayed(householdIncomeError, driver);
            return householdIncomeError.getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasExistingLoanError() {
        try {
            return existingLoanInstallmentError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public boolean hasAnyError() {
        return hasAgeError() || hasPropertyValueError() ||
                hasHouseholdIncomeError() || hasExistingLoanError();
    }

    //Results check

    //Function that checks if the calculation results are present
    public boolean isResultDisplayed() {
        try {
            return maxLoanAmount1.isDisplayed() && maxLoanAmount2.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCallbackButtonVisible() {
        ElementActions.waitForElementToBeDisplayed(callbackButton, driver);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", callbackButton);
        try {
            return callbackButton.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void waitForResultsToLoad() {
        try {
            // Wait for at least one result box
            ElementActions.waitForElementToBeDisplayed(maxLoanAmount1, driver);
            reporter.info("Results loaded successfully");
        } catch (Exception e) {
            reporter.warning("Results did not load in time");
        }
    }
    public boolean areAllResultsDisplayed() {
        try {
            return maxLoanAmount1.isDisplayed() &&
                    maxLoanAmount2.isDisplayed() &&
                    thmValue1.isDisplayed() &&
                    thmValue2.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    //Checkbox state checks

    public boolean isRegularIncomeChecked() {
        return regularIncomeCheckbox.isSelected();
    }

    public boolean isBabyExpectingChecked() {
        return babyExpectingCheckbox.isSelected();
    }

    public boolean isLoanProtectionInsuranceChecked() {
        return loanProtectionInsuranceCheckbox.isSelected();
    }

    // *** RESULT GETTERS - BOX 1 (10 year interest period) ***

    public int getBox1LoanAmount() {
        try {
            WebElement element = driver.findElement(By.id("box_1_max_desktop"));
            ElementActions.waitForElementToBeDisplayed(element, driver);
            String text = element.getText().trim();
            return Integer.parseInt(text.replaceAll("\\s+", ""));
        } catch (Exception e) {
            reporter.warning("Could not get Box 1 loan amount");
            return 0;
        }
    }



    public int getBox1MonthlyInstallment() {
        try {
            ElementActions.waitForElementToBeDisplayed(monthlyInstallment1, driver);
            String text = monthlyInstallment1.getText().trim();
            String cleanedText = text.replaceAll("\\s+", "").replace("Ft", "");
            return Integer.parseInt(cleanedText);
        } catch (Exception e) {
            reporter.warning("Could not get Box 1 monthly installment");
            return 0;
        }
    }

    //Function that gets the value of the THM element, converting it into floating point format, if it fails, returns 0
    public double getBox1THM() {
        try {
            ElementActions.waitForElementToBeDisplayed(thmValue1, driver);
            String text = thmValue1.getText();
            return Double.parseDouble(text.replace(",", "."));
        } catch (Exception e) {
            reporter.warning("Could not get Box 1 THM");
            return 0.0;
        }
    }


// === RESULT GETTERS - BOX 2 (20 year fixed interest) ===

    public int getBox2LoanAmount() {
        try {
            WebElement element = driver.findElement(By.id("box_2_max_desktop"));
            ElementActions.waitForElementToBeDisplayed(element, driver);
            String text = element.getText().trim();
            return Integer.parseInt(text.replaceAll("\\s+", ""));
        } catch (Exception e) {
            reporter.warning("Could not get Box 2 loan amount");
            return 0;
        }
    }

    public int getBox2MonthlyInstallment() {
        try {
            ElementActions.waitForElementToBeDisplayed(monthlyInstallment2, driver);
            String text = monthlyInstallment2.getText().trim();
            String cleanedText = text.replaceAll("\\s+", "").replace("Ft", "");
            return Integer.parseInt(cleanedText);
        } catch (Exception e) {
            reporter.warning("Could not get Box 2 monthly installment");
            return 0;
        }
    }

    public double getBox2THM() {
        try {
            ElementActions.waitForElementToBeDisplayed(thmValue2, driver);
            String text = thmValue2.getText();
            return Double.parseDouble(text.replace(",", "."));
        } catch (Exception e) {
            reporter.warning("Could not get Box 2 THM");
            return 0.0;
        }
    }


// *** HELPER METHOD ***

    public boolean hasCalculationResult() {
        try {
            ElementActions.waitForElementToBeDisplayed(maxLoanAmount1, driver);
            return maxLoanAmount1.isDisplayed() && maxLoanAmount2.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }


}