package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.DangNhapData;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class cho chức năng Đăng nhập - SauceDemo
 * Sử dụng DataProvider từ DangNhapData.
 */
public class TC_DangNhapTest extends BaseTest {

    /**
     * TC_DN_001: Đăng nhập thành công với tài khoản hợp lệ
     */
    @Test(dataProvider = "validLoginData", dataProviderClass = DangNhapData.class,
            description = "Đăng nhập thành công với tài khoản hợp lệ")
    public void TC_DN_001_dangNhapThanhCong(String username, String password, String expectedResult) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isLoginSuccess(),
                "Đăng nhập với " + username + " phải thành công");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Sau đăng nhập phải hiển thị trang Products");
    }

    /**
     * TC_DN_002: Đăng nhập thất bại với dữ liệu không hợp lệ
     */
    @Test(dataProvider = "invalidLoginData", dataProviderClass = DangNhapData.class,
            description = "Đăng nhập thất bại với dữ liệu không hợp lệ")
    public void TC_DN_002_dangNhapThatBai(String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login(username, password);

        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError,
                "Thông báo lỗi không đúng khi đăng nhập với user='" + username + "', pass='" + password + "'");
    }

    /**
     * TC_DN_003: Kiểm tra tất cả loại user trên SauceDemo
     */
    @Test(dataProvider = "allUsersData", dataProviderClass = DangNhapData.class,
            description = "Kiểm tra đăng nhập với tất cả loại user")
    public void TC_DN_003_kiemTraTatCaUser(String username, String password, boolean shouldSuccess) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login(username, password);

        if (shouldSuccess) {
            Assert.assertTrue(loginPage.isLoginSuccess(),
                    "User " + username + " phải đăng nhập thành công");
        } else {
            Assert.assertFalse(loginPage.isLoginSuccess(),
                    "User " + username + " không được đăng nhập thành công");
        }
    }
}
