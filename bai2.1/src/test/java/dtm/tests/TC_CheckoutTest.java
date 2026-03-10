package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.GioHangData;
import dtm.pages.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Test class cho chức năng Thanh toán (Checkout) - SauceDemo
 * Sử dụng DataProvider từ GioHangData.
 */
public class TC_CheckoutTest extends BaseTest {

    @Override
    @BeforeMethod
    public void setUp(Method method) {
        super.setUp(method);
        // Đăng nhập + thêm 1 sản phẩm vào giỏ
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();
    }

    /**
     * TC_CO_001: Checkout thành công với thông tin hợp lệ
     */
    @Test(dataProvider = "validCheckoutData", dataProviderClass = GioHangData.class,
            description = "Checkout thành công với thông tin hợp lệ")
    public void TC_CO_001_checkoutThanhCong(String firstName, String lastName, String postalCode) {
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo(firstName, lastName, postalCode);
        checkoutPage.clickContinue();

        // Kiểm tra ở trang Overview
        Assert.assertTrue(checkoutPage.isOnOverviewPage(),
                "Phải chuyển sang trang Checkout Overview");

        // Kiểm tra total > 0
        double total = checkoutPage.getTotal();
        Assert.assertTrue(total > 0, "Tổng tiền phải > 0");

        // Finish
        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderComplete(),
                "Đặt hàng phải thành công");
    }

    /**
     * TC_CO_002: Checkout thất bại khi thiếu thông tin
     */
    @Test(dataProvider = "invalidCheckoutData", dataProviderClass = GioHangData.class,
            description = "Checkout thất bại khi thiếu thông tin bắt buộc")
    public void TC_CO_002_checkoutThieuThongTin(String firstName, String lastName,
                                                 String postalCode, String expectedError) {
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo(firstName, lastName, postalCode);
        checkoutPage.clickContinue();

        String actualError = checkoutPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError,
                "Thông báo lỗi checkout không đúng");
    }

    /**
     * TC_CO_003: Hủy checkout quay lại giỏ hàng
     */
    @Test(description = "Hủy checkout quay lại trang giỏ hàng")
    public void TC_CO_003_huyCheckout() {
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.clickCancel();

        CartPage cartPage = new CartPage(getDriver());
        Assert.assertTrue(cartPage.isOnCartPage(),
                "Hủy checkout phải quay lại trang giỏ hàng");
    }

    /**
     * TC_CO_004: Kiểm tra tính toán tổng tiền chính xác
     */
    @Test(description = "Kiểm tra subtotal + tax = total")
    public void TC_CO_004_kiemTraTongTien() {
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo("Test", "User", "700000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isOnOverviewPage(), "Phải ở trang Overview");

        double subtotal = checkoutPage.getSubtotal();
        double tax = checkoutPage.getTax();
        double total = checkoutPage.getTotal();

        // total = subtotal + tax (sai số do làm tròn)
        Assert.assertEquals(total, subtotal + tax, 0.01,
                "Total phải bằng Subtotal + Tax");
    }

    /**
     * TC_CO_005: Checkout hoàn thành rồi quay về trang chủ
     */
    @Test(description = "Sau khi hoàn thành checkout, click Back Home về trang Products")
    public void TC_CO_005_backHomeSauCheckout() {
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo("Test", "User", "700000");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderComplete(), "Đặt hàng phải thành công");

        checkoutPage.clickBackHome();
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Phải quay lại trang Products sau khi click Back Home");
    }
}
