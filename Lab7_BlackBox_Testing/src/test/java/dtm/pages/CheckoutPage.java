package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object - Trang thanh toán / Checkout
 * Sử dụng driver.findElement() trực tiếp để tránh lỗi PageFactory proxy.
 */
public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Lấy tiêu đề trang */
    public String getTitle() {
        return driver.findElement(By.className("title")).getText();
    }

    /** Nhập thông tin checkout */
    public CheckoutPage fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        WebElement fn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        fn.clear();
        fn.sendKeys(firstName);

        WebElement ln = driver.findElement(By.id("last-name"));
        ln.clear();
        ln.sendKeys(lastName);

        WebElement pc = driver.findElement(By.id("postal-code"));
        pc.clear();
        pc.sendKeys(postalCode);
        return this;
    }

    /** Click nút Continue (dùng findElement trực tiếp) */
    public CheckoutPage clickContinue() {
        driver.findElement(By.id("continue")).click();
        return this;
    }

    /** Click nút Finish */
    public CheckoutPage clickFinish() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();
        return this;
    }

    /** Click nút Cancel */
    public void clickCancel() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cancel"))).click();
    }

    /** Lấy thông báo lỗi */
    public String getErrorMessage() {
        WebElement err = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h3[data-test='error']")));
        return err.getText();
    }

    /** Kiểm tra thông báo lỗi có hiển thị không */
    public boolean isErrorDisplayed() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h3[data-test='error']")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Lấy thông báo hoàn thành đặt hàng */
    public String getCompleteHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("complete-header"))).getText();
    }

    /** Kiểm tra đã hoàn thành checkout */
    public boolean isOrderComplete() {
        try {
            return driver.findElement(By.className("complete-header")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Lấy tổng tiền label */
    public String getTotalPrice() {
        return driver.findElement(By.className("summary_total_label")).getText();
    }

    /** Lấy Item total (trước thuế) */
    public double getItemTotal() {
        try {
            String text = driver.findElement(By.className("summary_subtotal_label"))
                    .getText().replace("Item total: $", "");
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Lấy Tax */
    public double getTax() {
        try {
            String text = driver.findElement(By.className("summary_tax_label"))
                    .getText().replace("Tax: $", "");
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Lấy Total */
    public double getTotal() {
        try {
            String text = driver.findElement(By.className("summary_total_label"))
                    .getText().replace("Total: $", "");
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Lấy nội dung trang complete */
    public String getCompleteText() {
        try {
            return driver.findElement(By.className("complete-text")).getText();
        } catch (Exception e) {
            return null;
        }
    }

    /** Click nút Back Home */
    public void clickBackHome() {
        try {
            driver.findElement(By.id("back-to-products")).click();
        } catch (Exception e) {
            // không tìm thấy
        }
    }

    /** Lấy Shipping Information */
    public String getShippingInfo() {
        try {
            WebElement el = driver.findElement(
                    By.cssSelector(".summary_info .summary_value_label:nth-of-type(2)"));
            return el.getText();
        } catch (Exception e) {
            try {
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
        List<String> names = new ArrayList<>();
        for (WebElement el : items) {
            names.add(el.getText());
        }
        return names;
    }

    /** Lấy danh sách giá items trong checkout overview */
    public List<Double> getItemPrices() {
        List<WebElement> items = driver.findElements(By.className("inventory_item_price"));
        List<Double> prices = new ArrayList<>();
        for (WebElement el : items) {
            prices.add(Double.parseDouble(el.getText().replace("$", "")));
        }
        return prices;
    }
}
