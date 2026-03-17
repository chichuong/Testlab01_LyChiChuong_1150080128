package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By submitButton = By.id("submit");
    private By errorMessage = By.id("error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        if (username != null && !username.isEmpty()) {
            enterText(usernameField, username);
        } else {
            driver.findElement(usernameField).clear();
        }
    }

    public void enterPassword(String password) {
        if (password != null && !password.isEmpty()) {
            enterText(passwordField, password);
        } else {
            driver.findElement(passwordField).clear();
        }
    }

    public void clickSubmit() {
        click(submitButton);
    }

    public String getErrorMessage() {
        if (isElementDisplayed(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
    
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
    }
}
