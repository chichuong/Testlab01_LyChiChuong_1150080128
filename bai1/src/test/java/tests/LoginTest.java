package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginTest extends BaseTest {
    
    // Yêu cầu: "Chạy test fail có ý thức (sai assertion) → ảnh chụp màn hình xuất hiện trong target/screenshots/"
    @Test
    public void testLoginFailureDisplayScreenshot() {
        // Yêu cầu: "Tất cả dữ liệu test (URL, username, password) phải đọc từ file config hoặc Excel/JSON"
        String url = ConfigReader.getProperty("url");
        String username = ConfigReader.getProperty("username");
        String password = ConfigReader.getProperty("password");

        // Yêu cầu: "Mọi @Test phải sử dụng getDriver() từ BaseTest - không tạo WebDriver mới trong test class"
        getDriver().get(url);
        
        // Yêu cầu: "Mọi driver.findElement() phải nằm trong Page Object - không viết trực tiếp trong test"
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password + "_sai");
        
        // Fake Assertion error -> Sẽ sinh ra screenshot tự động thông qua @AfterMethod trong BaseTest
        Assert.assertTrue(false, "Cố tình fail test để demo tính năng chụp màn hình tự động!");
    }

    // Test để demo parallel thread-count="3" -> Mở tổng cộng 3 chrome session
    @Test
    public void testParallelInstance2() {
        getDriver().get(ConfigReader.getProperty("url"));
        LoginPage loginPage = new LoginPage(getDriver());
        // Thực hiện hành động cơ bản để giữ trình duyệt
        Assert.assertTrue(true);
    }

    // Test để demo parallel thread-count="3"
    @Test
    public void testParallelInstance3() {
        getDriver().get(ConfigReader.getProperty("url"));
        LoginPage loginPage = new LoginPage(getDriver());
        // Thực hiện hành động cơ bản để giữ trình duyệt
        Assert.assertTrue(true);
    }
}
