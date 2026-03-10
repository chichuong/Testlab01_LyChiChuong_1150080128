package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test class cho chức năng Tìm kiếm / Sắp xếp sản phẩm - SauceDemo
 * (SauceDemo không có search bar, nên test Sort thay thế)
 */
public class TC_TimKiemTest extends BaseTest {

    @Override
    @BeforeMethod
    public void setUp(Method method) {
        super.setUp(method);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");
    }

    /**
     * TC_TK_001: Kiểm tra trang hiển thị đầy đủ 6 sản phẩm
     */
    @Test(description = "Trang Inventory hiển thị đủ 6 sản phẩm")
    public void TC_TK_001_hienThiDuSanPham() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "Phải ở trang Products");
        Assert.assertEquals(inventoryPage.getProductCount(), 6,
                "Phải hiển thị đúng 6 sản phẩm");
    }

    /**
     * TC_TK_002: Sắp xếp sản phẩm theo tên A-Z
     */
    @Test(description = "Sắp xếp sản phẩm theo tên A → Z")
    public void TC_TK_002_sapXepTenAZ() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.sortBy("az");

        List<String> names = inventoryPage.getProductNames();
        List<String> sorted = names.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(names, sorted, "Sản phẩm phải được sắp xếp A → Z");
    }

    /**
     * TC_TK_003: Sắp xếp sản phẩm theo tên Z-A
     */
    @Test(description = "Sắp xếp sản phẩm theo tên Z → A")
    public void TC_TK_003_sapXepTenZA() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.sortBy("za");

        List<String> names = inventoryPage.getProductNames();
        List<String> sorted = names.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        Assert.assertEquals(names, sorted, "Sản phẩm phải được sắp xếp Z → A");
    }

    /**
     * TC_TK_004: Sắp xếp sản phẩm theo giá thấp → cao
     */
    @Test(description = "Sắp xếp sản phẩm theo giá thấp → cao")
    public void TC_TK_004_sapXepGiaThapCao() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.sortBy("lohi");

        List<Double> prices = inventoryPage.getProductPrices();
        List<Double> sorted = prices.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(prices, sorted, "Sản phẩm phải được sắp xếp giá thấp → cao");
    }

    /**
     * TC_TK_005: Sắp xếp sản phẩm theo giá cao → thấp
     */
    @Test(description = "Sắp xếp sản phẩm theo giá cao → thấp")
    public void TC_TK_005_sapXepGiaCaoThap() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.sortBy("hilo");

        List<Double> prices = inventoryPage.getProductPrices();
        List<Double> sorted = prices.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        Assert.assertEquals(prices, sorted, "Sản phẩm phải được sắp xếp giá cao → thấp");
    }
}
