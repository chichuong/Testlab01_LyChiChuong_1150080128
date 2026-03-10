package tests;

import base.BaseTest;
import data.DangNhapData;
import pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Yêu cầu 2.3: Viết TC_DangNhapTest sử dụng DataProvider
 *
 * Sử dụng switch/if trên ketQuaMongDoi để xác định loại kiểm tra:
 *   - THANH_CONG    : Assert URL chứa inventory.html
 *   - SAI_THONG_TIN : Assert thông báo lỗi chứa "Username and password do not match"
 *   - BI_KHOA       : Assert thông báo lỗi chứa "locked out"
 *   - TRUONG_TRONG  : Assert thông báo lỗi chứa "Username is required" hoặc "Password is required"
 */
public class TC_DangNhapTest extends BaseTest {

    @Test(dataProvider = "du_lieu_dang_nhap", dataProviderClass = DangNhapData.class,
          description = "Kiểm thử đăng nhập với DataProvider")
    public void kiemThuDangNhap(String username, String password, String ketQuaMongDoi, String moTa) {

        System.out.println("=== Test: " + moTa + " ===");
        System.out.println("Username: [" + username + "] | Password: [" + password + "] | Kết quả mong đợi: " + ketQuaMongDoi);

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.moTrangDangNhap();

        // Xử lý giá trị null: nếu null thì không nhập gì, chỉ click Login
        if (username != null && password != null) {
            loginPage.dangNhap(username, password);
        } else if (username == null && password != null) {
            // Chỉ nhập password, bỏ trống username
            loginPage.nhapPassword(password);
            loginPage.clickDangNhap();
        } else if (username != null && password == null) {
            // Chỉ nhập username, bỏ trống password
            loginPage.nhapUsername(username);
            loginPage.clickDangNhap();
        } else {
            // Cả hai đều null → chỉ click Login
            loginPage.clickDangNhap();
        }

        // Sử dụng switch để kiểm tra kết quả dựa trên ketQuaMongDoi
        switch (ketQuaMongDoi) {

            case "THANH_CONG":
                Assert.assertTrue(loginPage.isDangOTrangSanPham(),
                        "Mong đợi chuyển sang trang sản phẩm nhưng không thành công. Mô tả: " + moTa);
                System.out.println("=> KẾT QUẢ: Đăng nhập thành công ✓");
                break;

            case "SAI_THONG_TIN":
                String loiSaiThongTin = loginPage.layThongBaoLoi();
                Assert.assertTrue(
                        loiSaiThongTin.contains("Username and password do not match"),
                        "Mong đợi thông báo sai thông tin đăng nhập. Thực tế: " + loiSaiThongTin + ". Mô tả: " + moTa);
                System.out.println("=> KẾT QUẢ: Hiển thị lỗi sai thông tin ✓ → " + loiSaiThongTin);
                break;

            case "BI_KHOA":
                String loiBiKhoa = loginPage.layThongBaoLoi();
                Assert.assertTrue(
                        loiBiKhoa.contains("locked out"),
                        "Mong đợi thông báo tài khoản bị khóa. Thực tế: " + loiBiKhoa + ". Mô tả: " + moTa);
                System.out.println("=> KẾT QUẢ: Hiển thị lỗi tài khoản bị khóa ✓ → " + loiBiKhoa);
                break;

            case "TRUONG_TRONG":
                String loiTruongTrong = loginPage.layThongBaoLoi();
                Assert.assertTrue(
                        loiTruongTrong.contains("Username is required") ||
                        loiTruongTrong.contains("Password is required"),
                        "Mong đợi thông báo trường bắt buộc. Thực tế: " + loiTruongTrong + ". Mô tả: " + moTa);
                System.out.println("=> KẾT QUẢ: Hiển thị lỗi trường trống ✓ → " + loiTruongTrong);
                break;

            default:
                Assert.fail("Kết quả mong đợi không hợp lệ: " + ketQuaMongDoi);
        }
    }
}
