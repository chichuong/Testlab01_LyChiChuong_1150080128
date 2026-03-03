package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases cho chức năng Sắp xếp sản phẩm.
 * Website: https://www.saucedemo.com/
 */
public class TC_TimKiemTest extends BaseTest {

    @Override
    @BeforeMethod
    public void setUp(Method method) {
        super.setUp(method);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.moTrangDangNhap();
        loginPage.dangNhap("standard_user", "secret_sauce");
    }

    @Test(groups = { "smoke" })
    public void testHienThiDanhSachSanPham() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isOnInventoryPage());
        Assert.assertTrue(inventoryPage.getProductCount() > 0);
    }

    @Test(groups = { "regression" })
    public void testSapXepTheoTenAZ() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.sortSanPham("az");
        List<String> names = inventoryPage.layDanhSachTenSanPham();
        List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted);
        Assert.assertEquals(names, sorted, "Sản phẩm không sắp xếp A-Z!");
    }

    @Test(groups = { "regression" })
    public void testSapXepTheoTenZA() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.sortSanPham("za");
        List<String> names = inventoryPage.layDanhSachTenSanPham();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals(names, sorted, "Sản phẩm không sắp xếp Z-A!");
    }

    @Test(groups = { "regression" })
    public void testSapXepTheoGiaThapDenCao() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.sortSanPham("lohi");
        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        Assert.assertEquals(prices, sorted, "Giá không tăng dần!");
    }

    @Test(groups = { "regression" })
    public void testSapXepTheoGiaCaoDenThap() {
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.sortSanPham("hilo");
        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals(prices, sorted, "Giá không giảm dần!");
    }
}
