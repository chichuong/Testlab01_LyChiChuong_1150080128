package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.DangNhapData;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test method đọc dữ liệu từ DataProvider, thực hiện đăng nhập và Assert kết
 * quả.
 * Website: https://www.saucedemo.com/
 */
public class TC_DangNhapTest extends BaseTest {

    @Test(dataProvider = "du_lieu_dang_nhap", dataProviderClass = DangNhapData.class, description = "Kiểm thử đăng nhập với nhiều bộ dữ liệu")
    public void kiemThuDangNhap(String username, String password,
            String ketQuaMongDoi, String moTa) {

        // Khởi tạo LoginPage
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.moTrangDangNhap();

        // Xử lý giá trị null → coi như chuỗi rỗng (phải xử lý trước khi nhập)
        String safeUser = (username != null) ? username : "";
        String safePass = (password != null) ? password : "";

        // Thực hiện đăng nhập
        loginPage.dangNhap(safeUser, safePass);

        // Kiểm tra kết quả dựa vào ketQuaMongDoi
        // Dùng switch/if để xử lý các loại kết quả mong đợi khác nhau
        switch (ketQuaMongDoi) {

            case "THANH_CONG":
            case "THANH_CONG_UI_LOI":
            case "THANH_CONG_CHAM":
            case "THANH_CONG_LOI_CHUC_NANG": {
                // Assert: phải chuyển đến trang Inventory (/inventory.html)
                Assert.assertTrue(loginPage.isDangOTrangSanPham(),
                        "[" + moTa + "] Mong đợi chuyển đến trang sản phẩm nhưng KHÔNG thành công!");
                // Không được có thông báo lỗi
                Assert.assertNull(loginPage.layThongBaoLoi(),
                        "[" + moTa + "] Đăng nhập thành công nhưng vẫn có thông báo lỗi!");
                break;
            }

            case "BI_KHOA": {
                // Assert: phải hiển thị lỗi chứa 'locked out'
                String loi = loginPage.layThongBaoLoi();
                Assert.assertNotNull(loi,
                        "[" + moTa + "] Mong đợi có thông báo lỗi nhưng không tìm thấy!");
                Assert.assertTrue(loi.contains("locked out"),
                        "[" + moTa + "] Thông báo lỗi không chứa 'locked out'. Actual: " + loi);
                // Không được chuyển trang
                Assert.assertFalse(loginPage.isDangOTrangSanPham(),
                        "[" + moTa + "] Tài khoản bị khoá nhưng vẫn chuyển đến trang sản phẩm!");
                break;
            }

            case "SAI_THONG_TIN": {
                // Assert: phải hiển thị lỗi chứa 'do not match'
                String loi = loginPage.layThongBaoLoi();
                Assert.assertNotNull(loi,
                        "[" + moTa + "] Mong đợi có thông báo lỗi nhưng không tìm thấy!");
                Assert.assertTrue(loi.contains("do not match"),
                        "[" + moTa + "] Thông báo lỗi không chứa 'do not match'. Actual: " + loi);
                Assert.assertFalse(loginPage.isDangOTrangSanPham(),
                        "[" + moTa + "] Sai thông tin nhưng vẫn chuyển đến trang sản phẩm!");
                break;
            }

            case "TRUONG_TRONG": {
                // Assert: phải hiển thị lỗi chứa 'is required'
                String loi = loginPage.layThongBaoLoi();
                Assert.assertNotNull(loi,
                        "[" + moTa + "] Mong đợi có thông báo lỗi nhưng không tìm thấy!");
                Assert.assertTrue(loi.contains("is required"),
                        "[" + moTa + "] Thông báo lỗi không chứa 'is required'. Actual: " + loi);
                Assert.assertFalse(loginPage.isDangOTrangSanPham(),
                        "[" + moTa + "] Trường trống nhưng vẫn chuyển đến trang sản phẩm!");
                break;
            }

            default:
                Assert.fail("[" + moTa + "] ketQuaMongDoi không hợp lệ: " + ketQuaMongDoi);
        }

        System.out.println("  ✔ PASS: " + moTa);
    }
}
