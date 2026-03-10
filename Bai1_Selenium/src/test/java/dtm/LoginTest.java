package dtm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver("chrome");
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com");
    }

    @Test(description = "Dang nhap thanh cong voi user/pass hop le", groups = { "smoke", "regression" })
    public void testLoginSuccess() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        DriverFactory.takeScreenshot("LoginTest_testLoginSuccess");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/inventory.html"),
                "Dang nhap that bai! URL hien tai: " + currentUrl + ", mong doi chua '/inventory.html'");
    }

    @Test(description = "Dang nhap sai mat khau", groups = { "regression" })
    public void testLoginWrongPassword() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        DriverFactory.takeScreenshot("LoginTest_testLoginWrongPassword");
        Assert.assertTrue(errorMsg.isDisplayed(),
                "Thong bao loi khong hien thi khi nhap sai mat khau!");
        Assert.assertTrue(errorMsg.getText().contains("Username and password do not match"),
                "Noi dung thong bao loi khong dung! Thuc te: " + errorMsg.getText());
    }

    @Test(description = "Dang nhap bo trong username", groups = { "regression" })
    public void testLoginEmptyUsername() {
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        DriverFactory.takeScreenshot("LoginTest_testLoginEmptyUsername");
        Assert.assertEquals(errorMsg.getText(), "Epic sadface: Username is required",
                "Thong bao loi khong dung khi bo trong username! Thuc te: " + errorMsg.getText());
    }

    @Test(description = "Dang nhap bo trong password", groups = { "regression" })
    public void testLoginEmptyPassword() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        DriverFactory.takeScreenshot("LoginTest_testLoginEmptyPassword");
        Assert.assertEquals(errorMsg.getText(), "Epic sadface: Password is required",
                "Thong bao loi khong dung khi bo trong password! Thuc te: " + errorMsg.getText());
    }

    @Test(description = "Dang nhap voi locked_out_user", groups = { "regression" })
    public void testLoginLockedUser() {
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        DriverFactory.takeScreenshot("LoginTest_testLoginLockedUser");
        Assert.assertTrue(errorMsg.getText().contains("Sorry, this user has been locked out"),
                "Thong bao loi khong dung khi dung locked_out_user! Thuc te: " + errorMsg.getText());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
