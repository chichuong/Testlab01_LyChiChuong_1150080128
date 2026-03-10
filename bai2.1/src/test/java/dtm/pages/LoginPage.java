package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object - Trang đăng nhập (https://www.saucedemo.com/)
 */
public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // URL trang đăng nhập
    private static final String LOGIN_URL = "https://www.saucedemo.com/";

    // Locators
    @FindBy(id = "user-name")
    private WebElement txtUsername;

    @FindBy(id = "password")
    private WebElement txtPassword;

    @FindBy(id = "login-button")
    private WebElement btnLogin;

    @FindBy(css = "[data-test='error']")
    private WebElement lblError;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Mở trang đăng nhập
     */
    public LoginPage open() {
        driver.get(LOGIN_URL);
        return this;
    }

    /**
     * Nhập username
     */
    public LoginPage enterUsername(String username) {
        txtUsername.clear();
        if (username != null) {
            txtUsername.sendKeys(username);
        }
        return this;
    }

    /**
     * Nhập password
     */
    public LoginPage enterPassword(String password) {
        txtPassword.clear();
        if (password != null) {
            txtPassword.sendKeys(password);
        }
        return this;
    }

    /**
     * Click nút Login
     */
    public LoginPage clickLogin() {
        btnLogin.click();
        return this;
    }

    /**
     * Thực hiện đăng nhập đầy đủ
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
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

    /**
     * Kiểm tra có đang ở trang đăng nhập không
     */
    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().equals(LOGIN_URL)
                || driver.getCurrentUrl().equals(LOGIN_URL + "/");
    }

    /**
     * Kiểm tra đăng nhập thành công (chuyển sang trang inventory)
     */
    public boolean isLoginSuccess() {
        try {
            wait.until(ExpectedConditions.urlContains("inventory"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
