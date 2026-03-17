package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginTest extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        getDriver().get(ConfigReader.getProperty("url"));
        
        // Hoàn toàn không sử dụng driver.findElement() hay By.id()
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(
                ConfigReader.getProperty("username"), 
                ConfigReader.getProperty("password")
        );
        
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page sau khi login không hiển thị.");
    }

    @Test
    public void testInvalidLoginAlertsError() {
        getDriver().get(ConfigReader.getProperty("url"));
        
        LoginPage loginPage = new LoginPage(getDriver())
                .loginExpectingFailure(
                        ConfigReader.getProperty("username"), 
                        ConfigReader.getProperty("invalid_password")
                );
        
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Lỗi khi nhập sai mật khẩu không hiển thị.");
    }

    @Test
    public void testLockedOutUser() {
        getDriver().get(ConfigReader.getProperty("url"));
        
        LoginPage loginPage = new LoginPage(getDriver())
                .loginExpectingFailure(
                        ConfigReader.getProperty("locked_out_user"), 
                        ConfigReader.getProperty("password")
                );
        
        Assert.assertTrue(loginPage.getErrorMessage().contains("Epic sadface: Sorry, this user has been locked out."), 
                "Không hiển thị thông báo logi bị khóa cho locked_out_user.");
    }
}
