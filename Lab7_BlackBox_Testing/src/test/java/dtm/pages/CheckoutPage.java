package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * Page Object - Trang thanh toán / Checkout
 * (https://www.saucedemo.com/checkout-step-one.html)
 */
public class CheckoutPage {

    private final WebDriver driver;

    @FindBy(className = "title")
    private WebElement lblTitle;

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

    @FindBy(id = "finish")
    private WebElement btnFinish;

    @FindBy(css = "h3[data-test='error']")
    private WebElement lblError;

    @FindBy(className = "complete-header")
    private WebElement lblCompleteHeader;

    @FindBy(className = "summary_total_label")
    private WebElement lblTotal;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Lấy tiêu đề trang.
     */
    public String getTitle() {
        return lblTitle.getText();
    }

    /**
     * Nhập thông tin checkout.
     */
    public CheckoutPage fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        txtFirstName.clear();
        txtFirstName.sendKeys(firstName);
        txtLastName.clear();
        txtLastName.sendKeys(lastName);
        txtPostalCode.clear();
        txtPostalCode.sendKeys(postalCode);
        return this;
    }

    /**
     * Click nút Continue.
     */
    public CheckoutPage clickContinue() {
        btnContinue.click();
        return this;
    }

    /**
     * Click nút Finish.
     */
    public CheckoutPage clickFinish() {
        btnFinish.click();
        return this;
    }

    /**
     * Click nút Cancel.
     */
    public void clickCancel() {
        btnCancel.click();
    }

    /**
     * Lấy thông báo lỗi.
     */
    public String getErrorMessage() {
        return lblError.getText();
    }

    /**
     * Kiểm tra thông báo lỗi có hiển thị không.
     */
    public boolean isErrorDisplayed() {
        try {
            return lblError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy thông báo hoàn thành đặt hàng.
     */
    public String getCompleteHeader() {
        return lblCompleteHeader.getText();
    }

    /**
     * Kiểm tra đã hoàn thành checkout.
     */
    public boolean isOrderComplete() {
        try {
            return lblCompleteHeader.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy tổng tiền.
     */
    public String getTotalPrice() {
        return lblTotal.getText();
    }

    /** Lấy Item total (trước thuế) */
    public double getItemTotal() {
        try {
            WebElement el = driver.findElement(By.className("summary_subtotal_label"));
            // "Item total: $xx.xx"
            String text = el.getText().replace("Item total: $", "");
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Lấy Tax */
    public double getTax() {
        try {
            WebElement el = driver.findElement(By.className("summary_tax_label"));
            // "Tax: $xx.xx"
            String text = el.getText().replace("Tax: $", "");
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Lấy Total */
    public double getTotal() {
        try {
            // "Total: $xx.xx"
            String text = lblTotal.getText().replace("Total: $", "");
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Lấy nội dung trang complete */
    public String getCompleteText() {
        try {
            WebElement el = driver.findElement(By.className("complete-text"));
            return el.getText();
        } catch (Exception e) {
            return null;
        }
    }

    /** Click nút Back Home */
    public void clickBackHome() {
        try {
            WebElement btn = driver.findElement(By.id("back-to-products"));
            btn.click();
        } catch (Exception e) {
            // không tìm thấy
        }
    }

    /** Lấy Shipping Information */
    public String getShippingInfo() {
        try {
            WebElement el = driver.findElement(By.cssSelector(".summary_info .summary_value_label:nth-of-type(2)"));
            return el.getText();
        } catch (Exception e) {
            try {
                // fallback: lấy text chứa 'Pony Express'
                List<WebElement> labels = driver.findElements(By.className("summary_value_label"));
                for (WebElement l : labels) {
                    if (l.getText().contains("Pony Express"))
                        return l.getText();
                }
            } catch (Exception ex) {
            }
            return null;
        }
    }

    /** Lấy danh sách tên items trong checkout overview */
    public List<String> getItemNames() {
        List<WebElement> items = driver.findElements(By.className("inventory_item_name"));
        List<String> names = new java.util.ArrayList<>();
        for (WebElement el : items) {
            names.add(el.getText());
        }
        return names;
    }

    /** Lấy danh sách giá items trong checkout overview */
    public List<Double> getItemPrices() {
        List<WebElement> items = driver.findElements(By.className("inventory_item_price"));
        List<Double> prices = new java.util.ArrayList<>();
        for (WebElement el : items) {
            prices.add(Double.parseDouble(el.getText().replace("$", "")));
        }
        return prices;
    }
}
