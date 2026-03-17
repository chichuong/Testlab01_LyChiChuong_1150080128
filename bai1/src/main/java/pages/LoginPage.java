package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Không định nghĩa WebElements trực tiếp trong test, phải đặt ở Page Object
    private By usernameInput = By.id("username");
    private By passwordInput = By.id("password");
    private By loginBtn = By.cssSelector("button[type='submit'], #login");
    private By errorMessage = By.id("flash"); // id của thẻ message lỗi trên herokuapp

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        // Chờ trang tải đầy đủ (1)
        waitForPageLoad();
        
        // Demo scrollToElement (2) và waitAndType (3)
        scrollToElement(usernameInput);
        waitAndType(usernameInput, username);
        
        scrollToElement(passwordInput);
        waitAndType(passwordInput, password);
        
        // Demo getAttribute (4) và waitAndClick (5)
        String btnType = getAttribute(loginBtn, "type");
        System.out.println("Login button type is: " + btnType);
        waitAndClick(loginBtn);
    }

    public String getErrorMessage() {
        // Demo isElementVisible (6) và getText (7)
        if (isElementVisible(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
}
