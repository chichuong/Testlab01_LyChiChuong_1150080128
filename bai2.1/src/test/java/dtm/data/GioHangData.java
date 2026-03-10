package dtm.data;

import org.testng.annotations.DataProvider;

/**
 * DataProvider cho chức năng Giỏ hàng.
 * Cung cấp dữ liệu test cho các test case thêm/xóa sản phẩm.
 */
public class GioHangData {

    /**
     * Dữ liệu thêm 1 sản phẩm vào giỏ
     * {productName, expectedPrice}
     */
    @DataProvider(name = "addSingleItemData")
    public static Object[][] addSingleItemData() {
        return new Object[][]{
                {"Sauce Labs Backpack", 29.99},
                {"Sauce Labs Bike Light", 9.99},
                {"Sauce Labs Bolt T-Shirt", 15.99},
                {"Sauce Labs Fleece Jacket", 49.99},
                {"Sauce Labs Onesie", 7.99},
                {"Test.allTheThings() T-Shirt (Red)", 15.99},
        };
    }

    /**
     * Dữ liệu thêm nhiều sản phẩm vào giỏ
     * {productNames[], expectedTotalItems}
     */
    @DataProvider(name = "addMultipleItemsData")
    public static Object[][] addMultipleItemsData() {
        return new Object[][]{
                {new String[]{"Sauce Labs Backpack", "Sauce Labs Bike Light"}, 2},
                {new String[]{"Sauce Labs Bolt T-Shirt", "Sauce Labs Fleece Jacket", "Sauce Labs Onesie"}, 3},
                {new String[]{"Sauce Labs Backpack", "Sauce Labs Bike Light",
                        "Sauce Labs Bolt T-Shirt", "Sauce Labs Fleece Jacket",
                        "Sauce Labs Onesie", "Test.allTheThings() T-Shirt (Red)"}, 6},
        };
    }

    /**
     * Dữ liệu xóa sản phẩm khỏi giỏ
     * {productToAdd, productToRemove, expectedRemainingCount}
     */
    @DataProvider(name = "removeItemData")
    public static Object[][] removeItemData() {
        return new Object[][]{
                {"Sauce Labs Backpack", "Sauce Labs Backpack", 0},
                {"Sauce Labs Bike Light", "Sauce Labs Bike Light", 0},
        };
    }

    /**
     * Dữ liệu checkout thông tin hợp lệ
     * {firstName, lastName, postalCode}
     */
    @DataProvider(name = "validCheckoutData")
    public static Object[][] validCheckoutData() {
        return new Object[][]{
                {"Nguyen", "Van A", "700000"},
                {"Tran", "Thi B", "100000"},
        };
    }

    /**
     * Dữ liệu checkout thông tin không hợp lệ
     * {firstName, lastName, postalCode, expectedError}
     */
    @DataProvider(name = "invalidCheckoutData")
    public static Object[][] invalidCheckoutData() {
        return new Object[][]{
                {"", "Van A", "700000", "Error: First Name is required"},
                {"Nguyen", "", "700000", "Error: Last Name is required"},
                {"Nguyen", "Van A", "", "Error: Postal Code is required"},
        };
    }
}
