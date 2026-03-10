package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object cho cac trang checkout:
 * - /checkout-step-one.html (dien thong tin)
 * - /checkout-step-two.html (overview)
 * - /checkout-complete.html (hoan thanh)
 */
public class CheckoutPage {

    private WebDriver driver;

    // ===== Checkout Step 1 =====
    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private List<WebElement> errorMessages;

    // ===== Checkout Step 2 (Overview) =====
    @FindBy(css = ".cart_item")
    private List<WebElement> overviewItems;

    @FindBy(css = ".summary_subtotal_label")
    private WebElement itemTotalLabel;

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    @FindBy(css = ".summary_info_label.summary_value_label, [data-test='payment-info-value']")
    private List<WebElement> paymentInfoElements;

    @FindBy(css = "[data-test='shipping-info-value']")
    private List<WebElement> shippingInfoElements;

    @FindBy(id = "finish")
    private WebElement finishButton;

    // ===== Checkout Complete =====
    @FindBy(css = ".complete-header")
    private List<WebElement> completeHeaders;

    @FindBy(css = ".complete-text")
    private List<WebElement> completeTexts;

    @FindBy(id = "back-to-products")
    private List<WebElement> backHomeButtons;

    // ===== Constructor =====
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ===== Step 1: Dien thong tin =====
    public void dienThongTin(String firstName, String lastName, String postalCode) {
        firstNameField.clear();
        if (firstName != null) firstNameField.sendKeys(firstName);
        lastNameField.clear();
        if (lastName != null) lastNameField.sendKeys(lastName);
        postalCodeField.clear();
        if (postalCode != null) postalCodeField.sendKeys(postalCode);
    }

    public void clickContinue() {
        continueButton.click();
        // Doi 1 chut de trang chuyen tiep hoac hien loi
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    public void clickCancel() {
        cancelButton.click();
    }

    public String layThongBaoLoi() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement err = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
            return err.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean coThongBaoLoi() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ===== Step 2: Overview =====
    public double layItemTotal() {
        String text = itemTotalLabel.getText();
        // "Item total: $xx.xx"
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    public double layTax() {
        String text = taxLabel.getText();
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    public double layTotal() {
        String text = totalLabel.getText();
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    public int laySoLuongItemOverview() {
        return overviewItems.size();
    }

    public List<String> layDanhSachTenItemOverview() {
        List<String> names = new ArrayList<>();
        for (WebElement item : overviewItems) {
            names.add(item.findElement(By.cssSelector(".inventory_item_name")).getText());
        }
        return names;
    }

    public void clickFinish() {
        finishButton.click();
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    // ===== Checkout Complete =====
    public boolean isOrderComplete() {
        return !completeHeaders.isEmpty() &&
               completeHeaders.get(0).getText().contains("Thank you for your order");
    }

    public String layCompleteHeader() {
        if (completeHeaders.isEmpty()) return "";
        return completeHeaders.get(0).getText();
    }

    public String layCompleteText() {
        if (completeTexts.isEmpty()) return "";
        return completeTexts.get(0).getText();
    }

    public void clickBackHome() {
        if (!backHomeButtons.isEmpty()) {
            backHomeButtons.get(0).click();
        }
    }

    // ===== Navigation checks =====
    public boolean isDangOCheckoutStep1() {
        return driver.getCurrentUrl().contains("checkout-step-one.html");
    }

    public boolean isDangOCheckoutStep2() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
            return true;
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("checkout-step-two.html");
        }
    }

    public boolean isDangOCheckoutComplete() {
        return driver.getCurrentUrl().contains("checkout-complete.html");
    }

    public String layShippingInfo() {
        if (shippingInfoElements.isEmpty()) return "";
        return shippingInfoElements.get(0).getText();
    }
}
