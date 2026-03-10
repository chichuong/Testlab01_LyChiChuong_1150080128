package data;

import org.testng.annotations.DataProvider;

/**
 * DataProvider chua du lieu kiem thu dang nhap saucedemo.com
 * Moi dong: {username, password, ketQuaMongDoi, moTa}
 */
public class DangNhapData {

    @DataProvider(name = "du_lieu_dang_nhap")
    public static Object[][] duLieuDangNhap() {
        return new Object[][] {

            // ===== 1. Tat ca tai khoan hop le =====
            {"standard_user",          "secret_sauce", "THANH_CONG",    "Dang nhap voi standard_user"},
            {"problem_user",           "secret_sauce", "THANH_CONG",    "Dang nhap voi problem_user"},
            {"performance_glitch_user","secret_sauce", "THANH_CONG",    "Dang nhap voi performance_glitch_user"},
            {"error_user",             "secret_sauce", "THANH_CONG",    "Dang nhap voi error_user"},
            {"visual_user",            "secret_sauce", "THANH_CONG",    "Dang nhap voi visual_user"},

            // ===== 2. Tai khoan bi khoa =====
            {"locked_out_user",        "secret_sauce", "BI_KHOA",       "Dang nhap voi tai khoan bi khoa"},

            // ===== 3. Tai khoan khong ton tai =====
            {"user_khong_ton_tai",     "matkhau123",   "SAI_THONG_TIN", "Username khong ton tai"},
            {"standard_user",          "sai_mat_khau", "SAI_THONG_TIN", "Password sai cho user hop le"},
            {"abc_xyz",                "xyz_abc",      "SAI_THONG_TIN", "Ca username va password deu sai"},

            // ===== 4. De trong username =====
            {"",                       "secret_sauce", "TRUONG_TRONG",  "De trong username"},

            // ===== 5. De trong password =====
            {"standard_user",          "",             "TRUONG_TRONG",  "De trong password"},

            // ===== 6. De trong ca hai =====
            {"",                       "",             "TRUONG_TRONG",  "De trong ca username va password"},

            // ===== 7. Username co ky tu dac biet =====
            {"user@#$%",              "secret_sauce",  "SAI_THONG_TIN", "Username chua ky tu dac biet @#$%"},
            {"<script>alert(1)</script>", "secret_sauce", "SAI_THONG_TIN", "Username chua the script (XSS)"},
            {"user'OR'1'='1",         "secret_sauce",  "SAI_THONG_TIN", "Username chua SQL injection"},

            // ===== 8. Khoang trang dau/cuoi =====
            {" standard_user",        "secret_sauce",  "SAI_THONG_TIN", "Username co khoang trang dau"},
            {"standard_user ",        "secret_sauce",  "SAI_THONG_TIN", "Username co khoang trang cuoi"},
            {" standard_user ",       "secret_sauce",  "SAI_THONG_TIN", "Username co khoang trang dau va cuoi"},

            // ===== 9. Gia tri null =====
            {null,                    "secret_sauce",  "TRUONG_TRONG",  "Username la null"},
            {"standard_user",         null,            "TRUONG_TRONG",  "Password la null"},
            {null,                    null,            "TRUONG_TRONG",  "Ca username va password la null"},
        };
    }
}
