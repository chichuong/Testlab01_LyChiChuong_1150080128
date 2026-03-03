package dtm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object - Trang đăng nhập (https://www.saucedemo.com/)
 * Mỗi trang web được đóng gói trong một class riêng – không viết trực tiếp
 * Selenium code trong test.
 */
public class LoginPage {

    private WebDriver driver;

    // Khai báo WebElement bằng @FindBy
    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "h3[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /** Mở trang đăng nhập */
    public void moTrangDangNhap() {
        driver.get("https://www.saucedemo.com/");
    }

    /** Nhập username vào ô user-name */
    public void nhapUsername(String username) {
        userNameField.clear();
        userNameField.sendKeys(username);
    }

    /** Nhập password vào ô password */
    public void nhapPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    /** Click nút Login */
    public void clickDangNhap() {
        loginButton.click();
    }

    /** Thực hiện đăng nhập đầy đủ */
    public void dangNhap(String user, String pass) {
        if (user != null && !user.isEmpty()) {
            nhapUsername(user);
        }
        if (pass != null && !pass.isEmpty()) {
            nhapPassword(pass);
        }
        clickDangNhap();
    }

    /** Trả về nội dung thông báo lỗi, null nếu không có lỗi */
    public String layThongBaoLoi() {
        try {
            if (errorMessage.isDisplayed()) {
                return errorMessage.getText();
            }
        } catch (Exception e) {
            // Không tìm thấy element → không có lỗi
        }
        return null;
    }

    /** Kiểm tra đã chuyển sang trang inventory chưa */
    public boolean isDangOTrangSanPham() {
        try {
            return driver.getCurrentUrl().contains("/inventory.html");
        } catch (Exception e) {
            return false;
        }
    }
}
