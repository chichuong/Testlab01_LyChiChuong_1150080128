package dtm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class CartTest {
    WebDriver driver;
    WebDriverWait wait;

    private void login() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver("chrome");
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com");
        login();
    }

    @Test(description = "Them san pham vao gio hang", groups = { "smoke", "regression" })
    public void testAddToCart() {
        driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click();

        WebElement cartBadge = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".shopping_cart_badge")));
        DriverFactory.takeScreenshot("CartTest_testAddToCart");
        Assert.assertEquals(cartBadge.getText(), "1",
                "So luong san pham trong gio hang khong dung! Mong doi: 1, Thuc te: " + cartBadge.getText());
    }

    @Test(description = "Xoa san pham khoi gio hang", groups = { "regression" })
    public void testRemoveFromCart() {
        // Them san pham truoc
        driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".shopping_cart_badge")));

        // Xoa san pham
        driver.findElement(By.cssSelector("[data-test='remove-sauce-labs-backpack']")).click();

        DriverFactory.takeScreenshot("CartTest_testRemoveFromCart");
        List<WebElement> badges = driver.findElements(By.cssSelector(".shopping_cart_badge"));
        Assert.assertTrue(badges.isEmpty(),
                "Gio hang van con san pham sau khi xoa!");
    }

    @Test(description = "Kiem tra trang gio hang hien thi dung san pham", groups = { "regression" })
    public void testCartPageDisplaysItem() {
        driver.findElement(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();

        wait.until(ExpectedConditions.urlContains("/cart.html"));
        WebElement cartItem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart_item")));
        DriverFactory.takeScreenshot("CartTest_testCartPageDisplaysItem");
        String itemName = cartItem.findElement(By.cssSelector(".inventory_item_name")).getText();
        Assert.assertEquals(itemName, "Sauce Labs Backpack",
                "Ten san pham trong gio hang khong dung! Thuc te: " + itemName);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
