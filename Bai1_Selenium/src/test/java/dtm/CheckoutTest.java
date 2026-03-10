package dtm;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class CheckoutTest {
    WebDriver driver;
    WebDriverWait wait;

    private void loginAndAddToCart() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("/inventory.html"));

        driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".shopping_cart_badge")));
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("/cart.html"));
    }

    private void setInputValue(String id, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "var el = document.getElementById(arguments[0]);" +
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;"
                        +
                        "nativeInputValueSetter.call(el, arguments[1]);" +
                        "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "el.dispatchEvent(new Event('change', { bubbles: true }));",
                id, value);
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver("chrome");
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com");
        loginAndAddToCart();
    }

    @Test(description = "Checkout thanh cong voi thong tin hop le", groups = { "smoke", "regression" })
    public void testCheckoutSuccess() {
        driver.findElement(By.cssSelector("[data-test='checkout']")).click();
        wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));

        setInputValue("first-name", "Nguyen");
        setInputValue("last-name", "VanA");
        setInputValue("postal-code", "700000");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();

        wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']"))).click();

        wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
        WebElement completeHeader = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header")));
        DriverFactory.takeScreenshot("CheckoutTest_testCheckoutSuccess");
        Assert.assertEquals(completeHeader.getText(), "Thank you for your order!",
                "Thong bao hoan thanh khong dung! Thuc te: " + completeHeader.getText());
    }

    @Test(description = "Checkout thieu thong tin - bo trong First Name", groups = { "regression" })
    public void testCheckoutMissingFirstName() {
        driver.findElement(By.cssSelector("[data-test='checkout']")).click();
        wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));

        // Bo trong firstName, chi nhap lastName va postalCode
        setInputValue("last-name", "VanA");
        setInputValue("postal-code", "700000");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();

        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        DriverFactory.takeScreenshot("CheckoutTest_testCheckoutMissingFirstName");
        Assert.assertTrue(errorMsg.getText().contains("First Name is required"),
                "Thong bao loi khong dung khi bo trong First Name! Thuc te: " + errorMsg.getText());
    }

    @Test(description = "Checkout thieu Postal Code", groups = { "regression" })
    public void testCheckoutMissingPostalCode() {
        driver.findElement(By.cssSelector("[data-test='checkout']")).click();
        wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));

        setInputValue("first-name", "Nguyen");
        setInputValue("last-name", "VanA");
        // Bo trong postalCode
        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();

        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        DriverFactory.takeScreenshot("CheckoutTest_testCheckoutMissingPostalCode");
        Assert.assertTrue(errorMsg.getText().contains("Postal Code is required"),
                "Thong bao loi khong dung khi bo trong Postal Code! Thuc te: " + errorMsg.getText());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
