package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;
import pages.CheckoutPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bai 2.3: Kiem thu Gio Hang va Thanh Toan - Luong End-to-End
 * Toi thieu 20 test case
 */
public class TC_GioHangTest extends BaseTest {

    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void chuanBi() {
        // Dang nhap truoc moi test
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.moTrangDangNhap();
        loginPage.dangNhap("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isDangOTrangSanPham(), "Khong dang nhap duoc");
    }

    // =====================================================
    // NHOM 1: THEM SAN PHAM VAO GIO HANG
    // =====================================================

    @Test(groups = {"smoke"}, description = "TC_CART_001: Them 1 san pham - badge = 1")
    public void themMotSanPham() {
        inventoryPage.themNSanPhamDauTien(1);
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 1, "Badge phai = 1");
    }

    @Test(groups = {"smoke"}, description = "TC_CART_002: Them 3 san pham - badge = 3")
    public void them3SanPham() {
        inventoryPage.themNSanPhamDauTien(3);
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 3, "Badge phai = 3");
    }

    @Test(groups = {"regression"}, description = "TC_CART_003: Them tat ca 6 san pham - badge = 6")
    public void themTatCa6SanPham() {
        inventoryPage.themNSanPhamDauTien(6);
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 6, "Badge phai = 6");
    }

    @Test(groups = {"smoke"}, description = "TC_CART_004: Them san pham theo ten va kiem tra trong gio")
    public void themSanPhamTheoTenKiemTraGio() {
        String tenSP = "Sauce Labs Backpack";
        inventoryPage.themSanPhamTheoTen(tenSP);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        List<String> dsGio = cartPage.layDanhSachTenSanPham();
        Assert.assertTrue(dsGio.contains(tenSP), "Gio hang phai chua san pham: " + tenSP);
    }

    @Test(groups = {"regression"}, description = "TC_CART_005: Them nhieu san pham - kiem tra danh sach trong gio")
    public void themNhieuSanPhamKiemTraDanhSach() {
        inventoryPage.themNSanPhamDauTien(3);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        Assert.assertEquals(cartPage.laySoLuongItem(), 3, "Gio hang phai co 3 item");
    }

    // =====================================================
    // NHOM 2: XOA SAN PHAM KHOI GIO HANG
    // =====================================================

    @Test(groups = {"regression"}, description = "TC_CART_006: Xoa 1 san pham - badge giam")
    public void xoaMotSanPham() {
        inventoryPage.themNSanPhamDauTien(3);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());

        // Xoa san pham dau tien
        List<String> dsBanDau = cartPage.layDanhSachTenSanPham();
        cartPage.xoaSanPhamTheoTen(dsBanDau.get(0));

        Assert.assertEquals(cartPage.laySoLuongItem(), 2, "Gio hang con 2 item");
        Assert.assertEquals(cartPage.laySoLuongBadge(), 2, "Badge phai = 2");
    }

    @Test(groups = {"regression"}, description = "TC_CART_007: Xoa het san pham - gio trong")
    public void xoaHetSanPham() {
        inventoryPage.themNSanPhamDauTien(3);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.xoaTatCaSanPham();

        Assert.assertEquals(cartPage.laySoLuongItem(), 0, "Gio hang phai trong");
        Assert.assertEquals(cartPage.laySoLuongBadge(), 0, "Badge phai = 0 (an)");
    }

    @Test(groups = {"regression"}, description = "TC_CART_008: Xoa het - nut Checkout van hien thi")
    public void xoaHetKiemTraCheckoutButton() {
        inventoryPage.themNSanPhamDauTien(2);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.xoaTatCaSanPham();

        // Nut Checkout van hien thi du gio trong
        Assert.assertTrue(cartPage.isCheckoutButtonVisible(), "Nut Checkout phai van hien thi");
    }

    // =====================================================
    // NHOM 3: SORT SAN PHAM
    // =====================================================

    @Test(groups = {"regression"}, description = "TC_CART_009: Sort Name A-Z")
    public void sortTenAZ() {
        inventoryPage.sortSanPham("az");
        List<String> names = inventoryPage.layDanhSachTenSanPham();
        List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted);
        Assert.assertEquals(names, sorted, "Danh sach phai sap xep A-Z");
    }

    @Test(groups = {"regression"}, description = "TC_CART_010: Sort Name Z-A")
    public void sortTenZA() {
        inventoryPage.sortSanPham("za");
        List<String> names = inventoryPage.layDanhSachTenSanPham();
        List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted, Collections.reverseOrder());
        Assert.assertEquals(names, sorted, "Danh sach phai sap xep Z-A");
    }

    @Test(groups = {"regression"}, description = "TC_CART_011: Sort Price low to high")
    public void sortGiaTangDan() {
        inventoryPage.sortSanPham("lohi");
        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        Assert.assertEquals(prices, sorted, "Gia phai sap xep tang dan");
    }

    @Test(groups = {"regression"}, description = "TC_CART_012: Sort Price high to low")
    public void sortGiaGiamDan() {
        inventoryPage.sortSanPham("hilo");
        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted, Collections.reverseOrder());
        Assert.assertEquals(prices, sorted, "Gia phai sap xep giam dan");
    }

    // =====================================================
    // NHOM 4: CHECKOUT FLOW
    // =====================================================

    @Test(groups = {"smoke"}, description = "TC_CART_013: Checkout dien du thong tin - thanh cong")
    public void checkoutThanhCong() {
        inventoryPage.themNSanPhamDauTien(2);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isDangOCheckoutStep1(), "Phai o trang checkout step 1");

        checkoutPage.dienThongTin("Nguyen", "Van A", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isDangOCheckoutStep2(), "Phai o trang checkout step 2");

        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderComplete(), "Dat hang phai thanh cong");
    }

    @Test(groups = {"regression"}, description = "TC_CART_014: Checkout de trong First Name - bao loi")
    public void checkoutTrongFirstName() {
        inventoryPage.themNSanPhamDauTien(1);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("", "Van B", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.coThongBaoLoi(), "Phai co thong bao loi");
        Assert.assertTrue(checkoutPage.layThongBaoLoi().contains("First Name"),
                "Thong bao phai lien quan den First Name");
    }

    @Test(groups = {"regression"}, description = "TC_CART_015: Checkout de trong Last Name - bao loi")
    public void checkoutTrongLastName() {
        inventoryPage.themNSanPhamDauTien(1);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("Nguyen", "", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.coThongBaoLoi(), "Phai co thong bao loi");
        Assert.assertTrue(checkoutPage.layThongBaoLoi().contains("Last Name"),
                "Thong bao phai lien quan den Last Name");
    }

    @Test(groups = {"regression"}, description = "TC_CART_016: Checkout de trong Postal Code - bao loi")
    public void checkoutTrongPostalCode() {
        inventoryPage.themNSanPhamDauTien(1);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("Nguyen", "Van C", "");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.coThongBaoLoi(), "Phai co thong bao loi");
        Assert.assertTrue(checkoutPage.layThongBaoLoi().contains("Postal Code"),
                "Thong bao phai lien quan den Postal Code");
    }

    @Test(groups = {"regression"}, description = "TC_CART_017: Checkout de trong tat ca - bao loi")
    public void checkoutTrongTatCa() {
        inventoryPage.themNSanPhamDauTien(1);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("", "", "");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.coThongBaoLoi(), "Phai co thong bao loi");
    }

    // =====================================================
    // NHOM 5: CANCEL NAVIGATION
    // =====================================================

    @Test(groups = {"regression"}, description = "TC_CART_018: Cancel o Step 1 - quay lai cart")
    public void cancelStep1() {
        inventoryPage.themNSanPhamDauTien(1);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.clickCancel();

        cartPage = new CartPage(getDriver());
        Assert.assertTrue(cartPage.isDangOTrangCart(), "Phai quay lai trang cart");
    }

    @Test(groups = {"regression"}, description = "TC_CART_019: Cancel o Step 2 - quay lai inventory")
    public void cancelStep2() {
        inventoryPage.themNSanPhamDauTien(1);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("Nguyen", "Van D", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isDangOCheckoutStep2(), "Phai o step 2");
        checkoutPage.clickCancel();

        inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isDangOTrangSanPham(), "Phai quay lai trang san pham");
    }

    @Test(groups = {"regression"}, description = "TC_CART_020: Continue Shopping tu gio hang - quay lai inventory")
    public void continueShopping() {
        inventoryPage.themNSanPhamDauTien(1);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickContinueShopping();

        inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isDangOTrangSanPham(), "Phai quay lai trang san pham");
    }

    // =====================================================
    // NHOM 6: SAU KHI HOAN THANH - GIO HANG RESET
    // =====================================================

    @Test(groups = {"smoke"}, description = "TC_CART_021: Sau khi hoan thanh - gio hang reset ve 0")
    public void sauHoanThanhGioHangReset() {
        inventoryPage.themNSanPhamDauTien(3);
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 3);

        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("Tran", "Van E", "70000");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderComplete(), "Dat hang thanh cong");

        // Click Back Home va kiem tra badge = 0
        checkoutPage.clickBackHome();
        inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isDangOTrangSanPham(), "Phai ve trang san pham");
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 0, "Gio hang phai reset ve 0");
    }

    // =====================================================
    // NHOM 7: KIEM TRA TINH TOAN TONG TIEN (Yeu cau 2.3.C)
    // =====================================================

    @Test(groups = {"regression"}, description = "TC_CART_022: Kiem tra tong tien chinh xac (Item total, Tax 8%, Total)")
    public void kiemTraTongTien() {
        // Them it nhat 3 san pham co gia khac nhau
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");       // $29.99
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light");     // $9.99
        inventoryPage.themSanPhamTheoTen("Sauce Labs Onesie");         // $7.99

        double tongGiaMongDoi = 29.99 + 9.99 + 7.99; // = 47.97

        // Vao trang checkout step 2
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("Test", "User", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isDangOCheckoutStep2(), "Phai o trang checkout step 2");

        // Lay itemTotal, tax, total tu trang
        double itemTotal = checkoutPage.layItemTotal();
        double tax = checkoutPage.layTax();
        double total = checkoutPage.layTotal();

        System.out.println("Item Total: $" + itemTotal);
        System.out.println("Tax: $" + tax);
        System.out.println("Total: $" + total);
        System.out.println("Tong gia mong doi: $" + tongGiaMongDoi);

        // Assert itemTotal = tong gia cac item
        Assert.assertEquals(itemTotal, tongGiaMongDoi, 0.01,
                "Item total phai = tong gia cac san pham");

        // Assert tax = itemTotal * 8%
        Assert.assertTrue(Math.abs(tax - itemTotal * 0.08) < 0.01,
                "Tax phai = 8% cua Item total. Tax thuc te: " + tax + ", mong doi: " + (itemTotal * 0.08));

        // Assert total = itemTotal + tax
        Assert.assertTrue(Math.abs(total - (itemTotal + tax)) < 0.01,
                "Total phai = Item total + Tax. Total thuc te: " + total + ", mong doi: " + (itemTotal + tax));
    }

    @Test(groups = {"regression"}, description = "TC_CART_023: Kiem tra tong tien voi tat ca 6 san pham")
    public void kiemTraTongTienTatCaSanPham() {
        // Them tat ca 6 san pham
        inventoryPage.themNSanPhamDauTien(6);

        // Lay gia tat ca san pham tu trang
        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        double tongGiaMongDoi = 0;
        for (double p : prices) tongGiaMongDoi += p;

        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("Full", "Test", "99999");
        checkoutPage.clickContinue();

        double itemTotal = checkoutPage.layItemTotal();
        double tax = checkoutPage.layTax();
        double total = checkoutPage.layTotal();

        Assert.assertEquals(itemTotal, tongGiaMongDoi, 0.01, "Item total phai bang tong gia");
        Assert.assertTrue(Math.abs(tax - itemTotal * 0.08) < 0.01, "Tax phai = 8%");
        Assert.assertTrue(Math.abs(total - (itemTotal + tax)) < 0.01, "Total phai chinh xac");
    }

    // =====================================================
    // NHOM 8: KIEM TRA VOI PROBLEM_USER
    // =====================================================

    @Test(groups = {"regression"}, description = "TC_CART_024: Dung problem_user - ghi nhan bug neu phat hien")
    public void kiemTraVoiProblemUser() {
        // Logout truoc
        inventoryPage.logout();

        // Dang nhap lai voi problem_user
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.moTrangDangNhap();
        loginPage.dangNhap("problem_user", "secret_sauce");

        inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isDangOTrangSanPham(), "problem_user phai dang nhap duoc");

        // Thu them san pham
        inventoryPage.themNSanPhamDauTien(1);
        int badge = inventoryPage.laySoLuongBadge();

        System.out.println("[PROBLEM_USER] Badge sau khi them 1 SP: " + badge);

        // Ghi nhan bug: problem_user co the co hanh vi bat thuong
        // Badge co the khong cap nhat dung, hinh anh san pham bi sai, v.v.
        if (badge != 1) {
            System.out.println("[BUG] problem_user: Badge khong = 1 sau khi them 1 san pham. Thuc te: " + badge);
        }

        // Kiem tra co the vao gio hang
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        int soItem = cartPage.laySoLuongItem();
        System.out.println("[PROBLEM_USER] So item trong gio: " + soItem);
    }

    @Test(groups = {"regression"}, description = "TC_CART_025: Kiem tra Shipping Info = Free Pony Express Delivery!")
    public void kiemTraShippingInfo() {
        inventoryPage.themNSanPhamDauTien(1);
        inventoryPage.clickGioHang();
        cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dienThongTin("Test", "Ship", "11111");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isDangOCheckoutStep2());
        String shippingInfo = checkoutPage.layShippingInfo();
        System.out.println("Shipping info: " + shippingInfo);
        Assert.assertTrue(shippingInfo.contains("Free Pony Express Delivery"),
                "Shipping phai la Free Pony Express Delivery! Thuc te: " + shippingInfo);
    }
}
