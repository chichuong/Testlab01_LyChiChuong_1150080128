package dtm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class DebugTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        DriverFactory.initDriver("chrome");
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com");
    }

    @Test
    public void debugCheckoutPage() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("/inventory.html"));

        driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("/cart.html"));

        driver.findElement(By.cssSelector("[data-test='checkout']")).click();
        wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));

        // Fill form - click each field before typing
        WebElement fn = wait.until(ExpectedConditions.elementToBeClickable(By.id("first-name")));
        fn.click();
        fn.sendKeys("Nguyen");

        WebElement ln = wait.until(ExpectedConditions.elementToBeClickable(By.id("last-name")));
        ln.click();
        ln.sendKeys("VanA");

        WebElement pc = wait.until(ExpectedConditions.elementToBeClickable(By.id("postal-code")));
        pc.click();
        pc.sendKeys("700000");

        System.out.println("=== VALUES AFTER SENDKEYS ===");
        System.out.println("first-name value: [" + fn.getAttribute("value") + "]");
        System.out.println("last-name value: [" + ln.getAttribute("value") + "]");
        System.out.println("postal-code value: [" + pc.getAttribute("value") + "]");

        // Click continue
        driver.findElement(By.id("continue")).click();

        // Wait a bit and check the URL
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        System.out.println("=== AFTER CLICK CONTINUE ===");
        System.out.println("Current URL: " + driver.getCurrentUrl());

        // Check for errors
        List<WebElement> errors = driver.findElements(By.cssSelector("[data-test='error']"));
        if (!errors.isEmpty()) {
            System.out.println("ERROR MSG: " + errors.get(0).getText());
        } else {
            System.out.println("No error messages found");
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
