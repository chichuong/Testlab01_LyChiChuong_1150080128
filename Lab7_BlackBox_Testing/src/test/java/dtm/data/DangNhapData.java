package dtm.data;

import org.testng.annotations.DataProvider;

/**
 * DataProvider cho các test case đăng nhập.
 * Website: https://www.saucedemo.com/
 *
 * Mỗi dòng: { username, password, ketQuaMongDoi, moTa }
 * ketQuaMongDoi: "THANH_CONG", "SAI_THONG_TIN", "BI_KHOA", "TRUONG_TRONG",
 * "THANH_CONG_UI_LOI", "THANH_CONG_CHAM", "THANH_CONG_LOI_CHUC_NANG"
 */
public class DangNhapData {

    @DataProvider(name = "du_lieu_dang_nhap")
    public static Object[][] getData() {
        return new Object[][] {
                // === TÀI KHOẢN HỢP LỆ (tất cả tài khoản do saucedemo cung cấp) ===
                { "standard_user", "secret_sauce", "THANH_CONG", "Đăng nhập thành công với standard_user" },
                { "problem_user", "secret_sauce", "THANH_CONG_UI_LOI", "Đăng nhập được nhưng UI sản phẩm lỗi" },
                { "performance_glitch_user", "secret_sauce", "THANH_CONG_CHAM", "Đăng nhập được, tải trang rất chậm" },
                { "error_user", "secret_sauce", "THANH_CONG_LOI_CHUC_NANG", "Đăng nhập được nhưng một số action lỗi" },
                { "visual_user", "secret_sauce", "THANH_CONG", "Đăng nhập thành công với visual_user" },

                // === TÀI KHOẢN BỊ KHOÁ ===
                { "locked_out_user", "secret_sauce", "BI_KHOA", "Tài khoản bị khoá" },

                // === TÀI KHOẢN KHÔNG TỒN TẠI ===
                { "khong_ton_tai", "matkhau123", "SAI_THONG_TIN", "Username không tồn tại" },
                { "abc_xyz", "wrong_pass", "SAI_THONG_TIN", "Username và password đều sai" },

                // === SAI PASSWORD ===
                { "standard_user", "sai_password", "SAI_THONG_TIN", "Đúng username, sai password" },

                // === ĐỂ TRỐNG ===
                { "", "", "TRUONG_TRONG", "Để trống cả username và password" },
                { "standard_user", "", "TRUONG_TRONG", "Để trống password" },
                { "", "secret_sauce", "TRUONG_TRONG", "Để trống username" },

                // === KÝ TỰ ĐẶC BIỆT ===
                { "!@#$%^&*()", "secret_sauce", "SAI_THONG_TIN", "Username chứa ký tự đặc biệt" },
                { "<script>alert(1)</script>", "secret_sauce", "SAI_THONG_TIN", "Username chứa script injection" },

                // === KHOẢNG TRẮNG ĐẦU/CUỐI ===
                { " standard_user", "secret_sauce", "SAI_THONG_TIN", "Username có khoảng trắng đầu" },
                { "standard_user ", "secret_sauce", "SAI_THONG_TIN", "Username có khoảng trắng cuối" },
                { "  ", "secret_sauce", "SAI_THONG_TIN", "Username chỉ chứa khoảng trắng" },

                // === GIÁ TRỊ NULL (phải xử lý trong test method) ===
                { null, "secret_sauce", "TRUONG_TRONG", "Username là null" },
                { "standard_user", null, "TRUONG_TRONG", "Password là null" },
                { null, null, "TRUONG_TRONG", "Cả username và password là null" },
        };
    }
}
