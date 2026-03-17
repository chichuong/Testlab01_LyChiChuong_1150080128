package core;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lớp nền tảng cho tất cả các Page Objects.
 * Bổ sung nạp chồng phương thức (Method Overloading) cho List<WebElement> & WebElement để hỗ trợ @FindBy của PageFactory.
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ==========================================
    // Methods hoạt động với By Locators
    // ==========================================
    public void waitAndClick(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void waitAndType(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    public boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (StaleElementReferenceException e) {
            try { return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed(); } catch (Exception ex) { return false; }
        } catch (Exception e) { return false; }
    }

    public void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public String getAttribute(By locator, String attributeName) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getAttribute(attributeName);
    }

    // ==========================================
    // Methods hoạt động với WebElement (@FindBy)
    // ==========================================
    public void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void waitAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).clear();
        element.sendKeys(text);
    }

    public String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText();
    }

    public boolean isElementVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (StaleElementReferenceException e) {
            try { return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed(); } catch (Exception ex) { return false; }
        } catch (Exception e) { return false; }
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public String getAttribute(WebElement element, String attributeName) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attributeName);
    }

    // ==========================================
    // Methods chung
    // ==========================================
    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }
}
