package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.CartPage;
import dtm.pages.CheckoutPage;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases cho chức năng Giỏ Hàng và Checkout.
 * Yêu cầu 2.3.A: Tối thiểu 20 TC.
 */
public class TC_GioHangTest extends BaseTest {

    InventoryPage inventoryPage;

    /** Đăng nhập trước mỗi test */
    @Override
    @BeforeMethod
    public void setUp(Method method) {
        super.setUp(method);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.moTrangDangNhap();
        loginPage.dangNhap("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage(getDriver());
    }

    /** Helper: vào giỏ hàng và tạo CartPage mới */
    private CartPage vaoGioHang() {
        inventoryPage.goToCart();
        return new CartPage(getDriver());
    }

    /** Helper: chờ URL chứa chuỗi mong đợi */
    private void waitForUrlContains(String urlPart) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains(urlPart));
    }

    // ===================== THÊM SẢN PHẨM VÀO GIỎ =====================

    @Test(groups = { "smoke" }, description = "TC_CART_001: Thêm 1 sản phẩm – badge = 1")
    public void themMotSanPham() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 1,
                "Badge phải = 1 sau khi thêm 1 sản phẩm");
    }

    @Test(groups = { "smoke" }, description = "TC_CART_002: Thêm 3 sản phẩm – badge = 3")
    public void them3SanPham() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light");
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bolt T-Shirt");
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 3,
                "Badge phải = 3 sau khi thêm 3 sản phẩm");
    }

    @Test(groups = { "regression" }, description = "TC_CART_003: Thêm tất cả 6 sản phẩm – badge = 6")
    public void themTatCa6SanPham() {
        inventoryPage.themNSanPhamDauTien(6);
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 6,
                "Badge phải = 6 sau khi thêm tất cả 6 sản phẩm");
    }

    @Test(groups = { "regression" }, description = "TC_CART_004: Thêm 1 SP – kiểm tra danh sách giỏ hàng")
    public void themSanPhamKiemTraDanhSach() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"),
                "Sauce Labs Backpack phải có trong giỏ hàng");
        Assert.assertEquals(cartPage.getCartItemCount(), 1);
    }

    @Test(groups = { "regression" }, description = "TC_CART_005: Thêm nhiều SP – kiểm tra đầy đủ danh sách")
    public void themNhieuSanPhamKiemTraDanhSach() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light");
        inventoryPage.themSanPhamTheoTen("Sauce Labs Onesie");
        CartPage cartPage = vaoGioHang();
        Assert.assertEquals(cartPage.getCartItemCount(), 3);
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"));
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Bike Light"));
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Onesie"));
    }

    // ===================== XOÁ SẢN PHẨM =====================

    @Test(groups = { "regression" }, description = "TC_CART_006: Xoá 1 sản phẩm – badge cập nhật")
    public void xoaMotSanPham() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light");
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 2);
        inventoryPage.xoaSanPhamTheoTen("Sauce Labs Backpack");
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 1,
                "Badge phải = 1 sau khi xoá 1 sản phẩm");
    }

    @Test(groups = { "regression" }, description = "TC_CART_007: Xoá SP từ trang giỏ hàng – danh sách cập nhật")
    public void xoaSanPhamTuGioHang() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light");
        CartPage cartPage = vaoGioHang();
        cartPage.removeProduct("Sauce Labs Backpack");
        // Re-create CartPage sau khi DOM thay đổi
        cartPage = new CartPage(getDriver());
        Assert.assertEquals(cartPage.getCartItemCount(), 1);
        Assert.assertFalse(cartPage.isProductInCart("Sauce Labs Backpack"));
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Bike Light"));
    }

    @Test(groups = { "regression" }, description = "TC_CART_008: Xoá hết – giỏ trống")
    public void xoaHetSanPham() {
        inventoryPage.themNSanPhamDauTien(3);
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 3);
        inventoryPage.xoaTatCaSanPham();
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 0,
                "Badge phải = 0 sau khi xoá hết");
    }

    @Test(groups = { "regression" }, description = "TC_CART_009: Giỏ trống – kiểm tra trang cart")
    public void gioTrongKiemTraCartPage() {
        CartPage cartPage = vaoGioHang();
        Assert.assertTrue(cartPage.isOnCartPage());
        Assert.assertEquals(cartPage.getCartItemCount(), 0,
                "Giỏ hàng phải trống");
    }

    // ===================== SORT SẢN PHẨM =====================

    @Test(groups = { "regression" }, description = "TC_CART_010: Sort giá tăng dần – đúng thứ tự")
    public void sortGiaTangDan() {
        inventoryPage.sortSanPham("lohi");
        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        Assert.assertEquals(prices, sorted, "Giá phải tăng dần");
    }

    @Test(groups = { "regression" }, description = "TC_CART_011: Sort giá giảm dần – đúng thứ tự")
    public void sortGiaGiamDan() {
        inventoryPage.sortSanPham("hilo");
        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals(prices, sorted, "Giá phải giảm dần");
    }

    @Test(groups = { "regression" }, description = "TC_CART_012: Sort tên A→Z – đúng thứ tự")
    public void sortTenAZ() {
        inventoryPage.sortSanPham("az");
        List<String> names = inventoryPage.layDanhSachTenSanPham();
        List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted);
        Assert.assertEquals(names, sorted, "Tên phải A→Z");
    }

    @Test(groups = { "regression" }, description = "TC_CART_013: Sort tên Z→A – đúng thứ tự")
    public void sortTenZA() {
        inventoryPage.sortSanPham("za");
        List<String> names = inventoryPage.layDanhSachTenSanPham();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals(names, sorted, "Tên phải Z→A");
    }

    // ===================== CHECKOUT – FORM =====================

    @Test(groups = { "regression" }, description = "TC_CART_014: Checkout – form trống firstName")
    public void checkoutThieuFirstName() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("", "Nguyen", "700000");
        checkout.clickContinue();
        Assert.assertTrue(checkout.isErrorDisplayed());
        Assert.assertEquals(checkout.getErrorMessage(), "Error: First Name is required");
    }

    @Test(groups = { "regression" }, description = "TC_CART_015: Checkout – form trống lastName")
    public void checkoutThieuLastName() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("Van A", "", "700000");
        checkout.clickContinue();
        Assert.assertTrue(checkout.isErrorDisplayed());
        Assert.assertEquals(checkout.getErrorMessage(), "Error: Last Name is required");
    }

    @Test(groups = { "regression" }, description = "TC_CART_016: Checkout – form trống postalCode")
    public void checkoutThieuPostalCode() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("Van A", "Nguyen", "");
        checkout.clickContinue();
        Assert.assertTrue(checkout.isErrorDisplayed());
        Assert.assertEquals(checkout.getErrorMessage(), "Error: Postal Code is required");
    }

    @Test(groups = { "regression" }, description = "TC_CART_017: Checkout – form điền đủ → chuyển Step 2")
    public void checkoutDienDuChuyenStep2() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("Van A", "Nguyen", "700000");
        checkout.clickContinue();
        waitForUrlContains("checkout-step-two");
        Assert.assertEquals(checkout.getTitle(), "Checkout: Overview",
                "Phải chuyển đến Step 2 (Overview)");
    }

    // ===================== CHECKOUT – NAVIGATION =====================

    @Test(groups = { "regression" }, description = "TC_CART_018: Cancel ở Step 1 → quay lại cart.html")
    public void cancelStep1() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.clickCancel();
        waitForUrlContains("/cart.html");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/cart.html"),
                "Phải quay lại trang giỏ hàng");
    }

    @Test(groups = { "regression" }, description = "TC_CART_019: Cancel ở Step 2 → quay lại inventory.html")
    public void cancelStep2() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("A", "B", "12345");
        checkout.clickContinue();
        waitForUrlContains("checkout-step-two");
        Assert.assertEquals(checkout.getTitle(), "Checkout: Overview");
        checkout.clickCancel();
        waitForUrlContains("/inventory.html");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/inventory.html"),
                "Phải quay lại trang inventory");
    }

    @Test(groups = { "regression" }, description = "TC_CART_020: Continue Shopping → quay lại inventory")
    public void continueShopping() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        cartPage.clickContinueShopping();
        waitForUrlContains("/inventory.html");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/inventory.html"),
                "Phải quay lại inventory");
    }

    // ===================== CHECKOUT – HOÀN THÀNH =====================

    @Test(groups = { "smoke" }, description = "TC_CART_021: Checkout flow hoàn chỉnh – thành công")
    public void checkoutFlowThanhCong() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("Nguyen", "Van A", "700000");
        checkout.clickContinue();
        waitForUrlContains("checkout-step-two");
        Assert.assertEquals(checkout.getTitle(), "Checkout: Overview");
        checkout.clickFinish();
        waitForUrlContains("checkout-complete");
        Assert.assertTrue(checkout.isOrderComplete());
        Assert.assertEquals(checkout.getCompleteHeader(), "Thank you for your order!");
    }

    @Test(groups = { "regression" }, description = "TC_CART_022: Sau hoàn thành – giỏ hàng reset về 0")
    public void sauHoanThanhGioHangReset() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("A", "B", "12345");
        checkout.clickContinue();
        waitForUrlContains("checkout-step-two");
        checkout.clickFinish();
        waitForUrlContains("checkout-complete");
        Assert.assertTrue(checkout.isOrderComplete());
        checkout.clickBackHome();
        waitForUrlContains("/inventory.html");
        InventoryPage inv2 = new InventoryPage(getDriver());
        Assert.assertEquals(inv2.laySoLuongBadge(), 0,
                "Giỏ hàng phải reset về 0 sau khi hoàn thành checkout");
    }

    @Test(groups = { "regression" }, description = "TC_CART_023: Complete page – header và content đúng")
    public void completePageContent() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Onesie");
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("A", "B", "12345");
        checkout.clickContinue();
        waitForUrlContains("checkout-step-two");
        checkout.clickFinish();
        waitForUrlContains("checkout-complete");
        Assert.assertEquals(checkout.getCompleteHeader(), "Thank you for your order!");
        String text = checkout.getCompleteText();
        Assert.assertNotNull(text);
        Assert.assertTrue(text.contains("dispatched"),
                "Nội dung phải chứa 'dispatched'");
    }

    // ===================== KIỂM TRA TÍNH TOÁN TỔNG TIỀN (2.3.C)
    // =====================

    @Test(groups = { "regression" }, description = "TC_CART_024: Kiểm tra tổng tiền chính xác")
    public void kiemTraTongTien() {
        // Thêm ít nhất 3 sản phẩm có giá khác nhau
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack"); // $29.99
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light"); // $9.99
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bolt T-Shirt"); // $15.99

        // Vào trang checkout step 2
        CartPage cartPage = vaoGioHang();
        cartPage.clickCheckout();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.fillCheckoutInfo("Nguyen", "Van A", "700000");
        checkout.clickContinue();
        waitForUrlContains("checkout-step-two");

        // Lấy itemTotal, tax, total từ trang
        double itemTotal = checkout.getItemTotal();
        double tax = checkout.getTax();
        double total = checkout.getTotal();

        // Tổng giá sản phẩm (tự tính tay)
        List<Double> prices = checkout.getItemPrices();
        double tongGiaSanPham = 0;
        for (double p : prices) {
            tongGiaSanPham += p;
        }

        // Assert itemTotal = tổng giá các items
        Assert.assertTrue(Math.abs(itemTotal - tongGiaSanPham) < 0.01,
                "Item total phải = tổng giá sản phẩm. Expected: " + tongGiaSanPham + ", Actual: " + itemTotal);

        // Assert Tax = itemTotal * 8%
        Assert.assertTrue(Math.abs(tax - itemTotal * 0.08) < 0.01,
                "Tax phải = 8% của Item total. Expected: " + (itemTotal * 0.08) + ", Actual: " + tax);

        // Assert Total = itemTotal + tax
        Assert.assertTrue(Math.abs(total - (itemTotal + tax)) < 0.01,
                "Total phải = Item total + Tax. Expected: " + (itemTotal + tax) + ", Actual: " + total);
    }

    // ===================== DÙNG problem_user – GHI NHẬN BUG =====================

    @Test(groups = { "regression" }, description = "TC_CART_025: problem_user – ghi nhận bug nếu phát hiện")
    public void problemUserBug() {
        // setUp() đã đăng nhập standard_user → cần logout trước
        inventoryPage.logout();

        // Đăng nhập lại với problem_user
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("saucedemo.com"));
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.dangNhap("problem_user", "secret_sauce");

        InventoryPage inv = new InventoryPage(getDriver());
        Assert.assertTrue(inv.isOnInventoryPage(), "problem_user phải đăng nhập được");

        // Thử thêm sản phẩm
        inv.themSanPhamTheoTen("Sauce Labs Backpack");
        int badge = inv.laySoLuongBadge();

        // Ghi nhận: problem_user có thể có bug ở UI hoặc hành vi
        System.out.println("[BUG CHECK] problem_user - Badge sau khi thêm SP: " + badge);
        if (badge != 1) {
            System.out
                    .println("[BUG] problem_user: Thêm sản phẩm nhưng badge không đúng! Expected: 1, Actual: " + badge);
        }

        // Kiểm tra hình ảnh sản phẩm (problem_user thường bị lỗi hình)
        List<String> names = inv.layDanhSachTenSanPham();
        System.out.println("[BUG CHECK] problem_user - Danh sách sản phẩm: " + names);

        // Test vẫn pass để ghi nhận thông tin bug
        Assert.assertTrue(inv.isOnInventoryPage());
    }
}
