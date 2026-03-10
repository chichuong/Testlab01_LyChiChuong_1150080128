package dtm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object - Trang thanh toán (https://www.saucedemo.com/checkout-step-one.html)
 */
public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // === Step One: Thông tin ===
    @FindBy(id = "first-name")
    private WebElement txtFirstName;

    @FindBy(id = "last-name")
    private WebElement txtLastName;

    @FindBy(id = "postal-code")
    private WebElement txtPostalCode;

    @FindBy(id = "continue")
    private WebElement btnContinue;

    @FindBy(id = "cancel")
    private WebElement btnCancel;

    @FindBy(css = "[data-test='error']")
    private WebElement lblError;

    // === Step Two: Tổng quan ===
    @FindBy(className = "title")
    private WebElement lblTitle;

    @FindBy(className = "summary_subtotal_label")
    private WebElement lblSubtotal;

    @FindBy(className = "summary_tax_label")
    private WebElement lblTax;

    @FindBy(className = "summary_total_label")
    private WebElement lblTotal;

    @FindBy(id = "finish")
    private WebElement btnFinish;

    // === Complete ===
    @FindBy(className = "complete-header")
    private WebElement lblCompleteHeader;

    @FindBy(className = "complete-text")
    private WebElement lblCompleteText;

    @FindBy(id = "back-to-products")
    private WebElement btnBackHome;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // === Step One Methods ===

    /**
     * Nhập thông tin checkout
     */
    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        txtFirstName.clear();
        if (firstName != null) txtFirstName.sendKeys(firstName);

        txtLastName.clear();
        if (lastName != null) txtLastName.sendKeys(lastName);

        txtPostalCode.clear();
        if (postalCode != null) txtPostalCode.sendKeys(postalCode);

        return this;
    }

    /**
     * Click Continue
     */
    public CheckoutPage clickContinue() {
        btnContinue.click();
        return this;
    }

    /**
     * Click Cancel
     */
    public void clickCancel() {
        btnCancel.click();
    }

    /**
     * Lấy thông báo lỗi
     */
    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(lblError));
            return lblError.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // === Step Two Methods ===

    /**
     * Kiểm tra đang ở trang Checkout Overview
     */
    public boolean isOnOverviewPage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(lblTitle));
            return lblTitle.getText().equals("Checkout: Overview");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy subtotal
     */
    public double getSubtotal() {
        String text = lblSubtotal.getText(); // "Item total: $29.99"
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Lấy tax
     */
    public double getTax() {
        String text = lblTax.getText();
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Lấy total
     */
    public double getTotal() {
        String text = lblTotal.getText();
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Click Finish
     */
    public CheckoutPage clickFinish() {
        btnFinish.click();
        return this;
    }

    // === Complete Methods ===

    /**
     * Kiểm tra đặt hàng thành công
     */
    public boolean isOrderComplete() {
        try {
            wait.until(ExpectedConditions.visibilityOf(lblCompleteHeader));
            return lblCompleteHeader.getText().contains("Thank you for your order");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy thông báo hoàn thành
     */
    public String getCompleteHeaderText() {
        return lblCompleteHeader.getText();
    }

    /**
     * Click Back Home
     */
    public void clickBackHome() {
        btnBackHome.click();
    }
}
