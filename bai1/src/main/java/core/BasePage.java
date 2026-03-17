package core;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lớp nền tảng cho tất cả các Page Objects.
 * Chứa các phương thức tương tác chung với các phần tử trên trang web.
 * Không sử dụng Thread.sleep() - tất cả đều dùng Explicit Wait.
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Chờ cho đến khi phần tử có thể click được và thực hiện click.
     * Sử dụng khi cần tương tác với các nút, liên kết, checkbox sau khi đã render xong.
     *
     * @param locator Bộ định vị của phần tử
     */
    public void waitAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    /**
     * Chờ cho đến khi phần tử hiển thị, xóa nội dung cũ (nếu có) và nhập văn bản mới.
     * Sử dụng cho các trường nhập liệu (input, textarea).
     *
     * @param locator Bộ định vị của phần tử
     * @param text    Đoạn văn bản cần nhập
     */
    public void waitAndType(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Chờ phần tử hiển thị và lấy nội dung văn bản của nó.
     * Sử dụng để lấy text kiểm tra kết quả hiển thị (assertion).
     *
     * @param locator Bộ định vị của phần tử
     * @return Văn bản (text) của phần tử
     */
    public String getText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }

    /**
     * Kiểm tra xem phần tử có đang hiển thị trên giao diện hay không.
     * Xử lý ngoại lệ StaleElementReferenceException - thực tế hay gặp khi trang render lại DOM,
     * đối tượng WebElement cũ bị mất gốc (detached from DOM).
     *
     * @param locator Bộ định vị của phần tử
     * @return true nếu hiển thị, false nếu không hoặc bị lỗi DOM thay đổi quá nhanh không thể khôi phục
     */
    public boolean isElementVisible(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (StaleElementReferenceException e) {
            // Thử tìm lại element và kiểm tra một lần nữa do DOM vừa được làm mới
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                return element.isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        } catch (Exception e) {
            // Bao gồm TimeoutException, NoSuchElementException...
            return false;
        }
    }

    /**
     * Cuộn trang web đến vị trí của phần tử mong muốn bằng JavascriptExecutor.
     * Sử dụng khi phần tử bị che khuất, bị banner đè lên, hoặc nằm ở cuối trang không thể tương tác mặc định.
     *
     * @param locator Bộ định vị của phần tử
     */
    public void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Chờ cho đến khi toàn bộ trang web được tải xong thông qua Javascript document.readyState.
     * Sử dụng sau khi chuyển trang, click nút submit form, hoặc làm mới trang để đảm bảo trang đã sẵn sàng.
     */
    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Lấy giá trị của một thuộc tính (attribute) từ phần tử.
     * Sử dụng khi cần kiểm tra các thuộc tính như 'value', 'href', 'src', 'class', 'type' v.v.
     *
     * @param locator Bộ định vị của phần tử
     * @param attributeName Tên thuộc tính cần lấy giá trị
     * @return Giá trị của thuộc tính dưới dạng chuỗi
     */
    public String getAttribute(By locator, String attributeName) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return element.getAttribute(attributeName);
    }
}
