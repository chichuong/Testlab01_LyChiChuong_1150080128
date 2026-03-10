package dtm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class TitleTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");
    }

    @Test(description = "Kiem thu tieu de trang chu")
    public void testTitle() {
        String expectedTitle = "Swag Labs";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Tieu de trang khong dung!");
    }

    @Test(description = "Kiem thu URL trang chu")
    public void testURL() {
        String actualUrl = driver.getCurrentUrl();
        Assert.assertTrue(actualUrl.contains("saucedemo"), "URL khong hop le!");
    }

    @Test(description = "Kiem thu nguon trang (page source)")
    public void testPageSource() {
        String pageSource = driver.getPageSource();
        Assert.assertNotNull(pageSource, "Page source bi null!");
        Assert.assertTrue(pageSource.contains("Swag Labs"), "Page source khong chua 'Swag Labs'!");
        Assert.assertTrue(pageSource.contains("login-button"), "Page source khong chua 'login-button'!");
    }

    @Test(description = "Kiem thu form dang nhap co hien thi hay khong")
    public void testLoginFormDisplayed() {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        Assert.assertTrue(usernameField.isDisplayed(), "Truong username khong hien thi!");
        Assert.assertTrue(passwordField.isDisplayed(), "Truong password khong hien thi!");
        Assert.assertTrue(loginButton.isDisplayed(), "Nut Login khong hien thi!");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
