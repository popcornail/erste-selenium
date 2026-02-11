package hu.robertszujo.seleniumproject.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.aventstack.extentreports.ExtentTest;
import hu.robertszujo.seleniumproject.constants.TestConstants;
import hu.robertszujo.seleniumproject.utils.ElementActions;
import org.openqa.selenium.support.FindBy;


public class LoanMaximumCalculatorPage extends BasePageObject {

    // === S ===

    // Basic data
    @FindBy(id = "meletkor")
    private WebElement ageInput;

    @FindBy(id = "ingatlan_erteke")
    private WebElement propertyValueInput;

    // Household
    @FindBy(id = "egyedul")
    private WebElement singleEarnerButton;

    @FindBy(id = "tobben")
    private WebElement multipleEarnersButton;

    @FindBy(id = "mjovedelem")
    private WebElement householdIncomeInput;

    // Existing loans
    @FindBy(id = "meglevo_torleszto")
    private WebElement existingLoanInstallmentInput;

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

    // Results / Errors
    @FindBy(id = "error_message")  // TODO
    private WebElement errorMessage;

    @FindBy(id = "max_loan_amount")  // TODO
    private WebElement maxLoanAmount;

    @FindBy(id = "thm_value")  // TODO
    private WebElement thmValue;

    // === CONSTRUCTOR ===
    public LoanMaximumCalculatorPage(WebDriver driver, ExtentTest reporter) {
        super(driver, reporter);
    }

}
