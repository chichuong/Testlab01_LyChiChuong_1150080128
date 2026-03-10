package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Yêu cầu 2.2: Page Object cho trang đăng nhập saucedemo.com
 */
public class LoginPage {

    private WebDriver driver;

    // ===== Khai báo @FindBy =====
    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ===== Constructor =====
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ===== Các phương thức thao tác =====

    /**
     * Mở trang đăng nhập saucedemo.com
     */
    public LoginPage moTrangDangNhap() {
        driver.get("https://www.saucedemo.com/");
        return this;
    }

    /**
     * Nhập username vào ô Username
     */
    public LoginPage nhapUsername(String username) {
        userNameField.clear();
        userNameField.sendKeys(username);
        return this;
    }

    /**
     * Nhập password vào ô Password
     */
    public LoginPage nhapPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
        return this;
    }

    /**
     * Click nút Đăng nhập (Login)
     */
    public LoginPage clickDangNhap() {
        loginButton.click();
        return this;
    }

    /**
     * Thực hiện đăng nhập đầy đủ: nhập username, password rồi click Login
     */
    public void dangNhap(String username, String password) {
        nhapUsername(username);
        nhapPassword(password);
        clickDangNhap();
    }

    /**
     * Lấy thông báo lỗi hiển thị trên trang
     */
    public String layThongBaoLoi() {
        return errorMessage.getText();
    }

    /**
     * Kiểm tra xem đã chuyển sang trang sản phẩm (inventory) chưa
     */
    public boolean isDangOTrangSanPham() {
        return driver.getCurrentUrl().contains("inventory.html");
    }
}
