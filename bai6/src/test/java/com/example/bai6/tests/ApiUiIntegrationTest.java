package com.example.bai6.tests;

import com.example.bai6.base.ApiBaseTest;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiUiIntegrationTest extends ApiBaseTest {

    private WebDriver driver;
    private String loginToken;
    private int loginStatus;
    private boolean isApiAlive;

    @BeforeMethod(alwaysRun = true)
    public void loginPreconditionBeforeEachMethod() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");

        Response response = given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/login")
                .andReturn();

        loginStatus = response.statusCode();
        String contentType = response.getContentType();
        if (contentType != null && contentType.toLowerCase().contains("json")) {
            loginToken = response.jsonPath().getString("token");
        } else {
            loginToken = null;
        }

        System.out.printf("[API-PRECONDITION] POST /login status=%d, token=%s%n", loginStatus, loginToken);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test(description = "API precondition check: POST /api/login must return 200 and token to allow UI verification")
    public void shouldPrepareLoginTokenFromApi() {
        if (loginStatus != 200 || loginToken == null || loginToken.trim().isEmpty()) {
            throw new SkipException("API login precondition failed, UI verification tests must be skipped.");
        }

        Assert.assertEquals(loginStatus, 200, "Expected login API status 200");
        Assert.assertNotNull(loginToken, "Expected token from login API");
    }

    @Test(dependsOnMethods = "shouldPrepareLoginTokenFromApi", description = "UI verification: login via form and verify inventory URL + title Swag Labs")
    public void shouldVerifyUiAfterSuccessfulApiLogin() {
        driver = createDriverOrSkip();

        // UI action: open login page and login with form fields.
        driver.get("https://www.saucedemo.com/");
        waitVisible(By.id("user-name")).sendKeys("standard_user");
        waitVisible(By.id("password")).sendKeys("secret_sauce");
        waitVisible(By.id("login-button")).click();

        // Assertion: verify inventory URL and page title.
        waitVisible(By.id("inventory_container"));
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "Expected URL to contain 'inventory'");
        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Expected page title to be 'Swag Labs'");
    }

    @Test(description = "API alive check: GET /api/users must return 200, then store result into isApiAlive")
    public void shouldCheckApiAliveFlag() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/users")
                .andReturn();

        isApiAlive = response.statusCode() == 200;
        System.out.printf("[API-CHECK] GET /users status=%d, isApiAlive=%s%n", response.statusCode(), isApiAlive);

        if (!isApiAlive) {
            throw new SkipException("API is not alive, skip full UI integration flow.");
        }

        Assert.assertTrue(isApiAlive, "API must be alive before running full UI flow");
    }

    @Test(dependsOnMethods = "shouldCheckApiAliveFlag", description = "Full integration flow: UI login, add 2 items, verify badge=2, verify cart has 2 products")
    public void shouldRunFullApiUiIntegrationFlow() {
        if (!isApiAlive) {
            throw new SkipException("isApiAlive=false, UI integration flow is skipped.");
        }

        driver = createDriverOrSkip();

        // UI action: login to saucedemo.
        driver.get("https://www.saucedemo.com/");
        waitVisible(By.id("user-name")).sendKeys("standard_user");
        waitVisible(By.id("password")).sendKeys("secret_sauce");
        waitVisible(By.id("login-button")).click();
        waitVisible(By.id("inventory_container"));

        // UI action: add two products to cart.
        waitVisible(By.id("add-to-cart-sauce-labs-backpack")).click();
        waitVisible(By.id("add-to-cart-sauce-labs-bike-light")).click();

        // Assertion: cart badge must equal 2.
        String badge = waitVisible(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(badge, "2", "Expected cart badge to be 2");

        // UI action: open cart and assert total products in cart.
        waitVisible(By.className("shopping_cart_link")).click();
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));

        // Assertion: cart should contain 2 products.
        Assert.assertEquals(cartItems.size(), 2, "Expected exactly 2 products in cart");
    }

    private WebDriver createDriverOrSkip() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            return new ChromeDriver(options);
        } catch (Exception ex) {
            throw new SkipException("Cannot start Chrome WebDriver in current environment: " + ex.getMessage());
        }
    }

    private WebElement waitVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
