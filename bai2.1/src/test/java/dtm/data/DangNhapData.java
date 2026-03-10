package dtm.data;

import org.testng.annotations.DataProvider;

/**
 * DataProvider cho chức năng Đăng nhập.
 * Cung cấp dữ liệu test cho các test case đăng nhập với SauceDemo.
 */
public class DangNhapData {

    /**
     * Dữ liệu đăng nhập hợp lệ
     * {username, password, expectedResult}
     */
    @DataProvider(name = "validLoginData")
    public static Object[][] validLoginData() {
        return new Object[][]{
                {"standard_user", "secret_sauce", "success"},
                {"visual_user", "secret_sauce", "success"},
        };
    }

    /**
     * Dữ liệu đăng nhập không hợp lệ
     * {username, password, expectedErrorMessage}
     */
    @DataProvider(name = "invalidLoginData")
    public static Object[][] invalidLoginData() {
        return new Object[][]{
                // Sai password
                {"standard_user", "wrong_password",
                        "Epic sadface: Username and password do not match any user in this service"},
                // Sai username
                {"invalid_user", "secret_sauce",
                        "Epic sadface: Username and password do not match any user in this service"},
                // Username trống
                {"", "secret_sauce",
                        "Epic sadface: Username is required"},
                // Password trống
                {"standard_user", "",
                        "Epic sadface: Password is required"},
                // Cả 2 trống
                {"", "",
                        "Epic sadface: Username is required"},
                // User bị khóa
                {"locked_out_user", "secret_sauce",
                        "Epic sadface: Sorry, this user has been locked out."},
        };
    }

    /**
     * Dữ liệu đăng nhập - tất cả user types trên SauceDemo
     * {username, password, shouldSuccess}
     */
    @DataProvider(name = "allUsersData")
    public static Object[][] allUsersData() {
        return new Object[][]{
                {"standard_user", "secret_sauce", true},
                {"locked_out_user", "secret_sauce", false},
                {"problem_user", "secret_sauce", true},
                {"performance_glitch_user", "secret_sauce", true},
                {"error_user", "secret_sauce", true},
                {"visual_user", "secret_sauce", true},
        };
    }
}
