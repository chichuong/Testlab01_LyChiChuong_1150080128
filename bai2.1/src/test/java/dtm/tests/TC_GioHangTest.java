package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.GioHangData;
import dtm.pages.CartPage;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Test class cho chức năng Giỏ hàng - SauceDemo
 * Sử dụng DataProvider từ GioHangData.
 */
public class TC_GioHangTest extends BaseTest {

    /**
     * Đăng nhập trước mỗi test
     */
    @Override
    @BeforeMethod
    public void setUp(Method method) {
        super.setUp(method);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");
    }

    /**
     * TC_GH_001: Thêm 1 sản phẩm vào giỏ hàng
     */
    @Test(dataProvider = "addSingleItemData", dataProviderClass = GioHangData.class,
            description = "Thêm 1 sản phẩm vào giỏ hàng và kiểm tra")
    public void TC_GH_001_themMotSanPham(String productName, double expectedPrice) {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName(productName);

        // Kiểm tra badge giỏ hàng = 1
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1,
                "Badge giỏ hàng phải hiển thị 1");

        // Vào giỏ hàng kiểm tra
        inventoryPage.goToCart();
        CartPage cartPage = new CartPage(getDriver());

        Assert.assertTrue(cartPage.isOnCartPage(), "Phải ở trang giỏ hàng");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Giỏ hàng phải có 1 sản phẩm");

        List<String> itemNames = cartPage.getCartItemNames();
        Assert.assertTrue(itemNames.contains(productName),
                "Giỏ hàng phải chứa sản phẩm: " + productName);

        double actualPrice = cartPage.getItemPrice(productName);
        Assert.assertEquals(actualPrice, expectedPrice,
                "Giá sản phẩm " + productName + " phải đúng");
    }

    /**
     * TC_GH_002: Thêm nhiều sản phẩm vào giỏ hàng
     */
    @Test(dataProvider = "addMultipleItemsData", dataProviderClass = GioHangData.class,
            description = "Thêm nhiều sản phẩm vào giỏ hàng")
    public void TC_GH_002_themNhieuSanPham(String[] productNames, int expectedCount) {
        InventoryPage inventoryPage = new InventoryPage(getDriver());

        for (String name : productNames) {
            inventoryPage.addToCartByName(name);
        }

        // Kiểm tra badge
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), expectedCount,
                "Badge giỏ hàng phải hiển thị " + expectedCount);

        // Vào giỏ kiểm tra
        inventoryPage.goToCart();
        CartPage cartPage = new CartPage(getDriver());
        Assert.assertEquals(cartPage.getCartItemCount(), expectedCount,
                "Giỏ hàng phải có " + expectedCount + " sản phẩm");
    }

    /**
     * TC_GH_003: Xóa sản phẩm khỏi giỏ hàng
     */
    @Test(dataProvider = "removeItemData", dataProviderClass = GioHangData.class,
            description = "Xóa sản phẩm khỏi giỏ hàng")
    public void TC_GH_003_xoaSanPham(String productToAdd, String productToRemove, int expectedRemaining) {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName(productToAdd);
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Giỏ phải có 1 sản phẩm trước khi xóa");

        cartPage.removeItemByName(productToRemove);

        Assert.assertEquals(cartPage.getCartItemCount(), expectedRemaining,
                "Giỏ hàng phải còn " + expectedRemaining + " sản phẩm sau khi xóa");
    }

    /**
     * TC_GH_004: Quay lại trang shopping từ giỏ hàng
     */
    @Test(description = "Click Continue Shopping quay lại trang sản phẩm")
    public void TC_GH_004_quayLaiShopping() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        Assert.assertTrue(cartPage.isOnCartPage(), "Phải ở trang giỏ hàng");

        cartPage.clickContinueShopping();

        inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Phải quay lại trang Products");
    }
}
