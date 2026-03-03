package dtm.data;

import org.testng.annotations.DataProvider;

/**
 * DataProvider cho các test case giỏ hàng.
 * Website: https://www.saucedemo.com/
 */
public class GioHangData {

    /**
     * Dữ liệu thêm sản phẩm vào giỏ hàng (tên sản phẩm, số lượng mong đợi trong
     * badge).
     */
    @DataProvider(name = "addToCart")
    public static Object[][] addToCartData() {
        return new Object[][] {
                { "Sauce Labs Backpack", "1" },
                { "Sauce Labs Bike Light", "1" },
        };
    }

    /**
     * Dữ liệu thêm nhiều sản phẩm vào giỏ hàng.
     */
    @DataProvider(name = "multipleProducts")
    public static Object[][] multipleProductsData() {
        return new Object[][] {
                { new String[] { "Sauce Labs Backpack", "Sauce Labs Bike Light" }, "2" },
                { new String[] { "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt" }, "3" },
        };
    }

    /**
     * Dữ liệu checkout (firstName, lastName, postalCode).
     */
    @DataProvider(name = "checkoutInfo")
    public static Object[][] checkoutInfoData() {
        return new Object[][] {
                { "Nguyen", "Van A", "700000" },
        };
    }

    /**
     * Dữ liệu checkout không hợp lệ.
     */
    @DataProvider(name = "invalidCheckoutInfo")
    public static Object[][] invalidCheckoutInfoData() {
        return new Object[][] {
                { "", "Van A", "700000", "Error: First Name is required" },
                { "Nguyen", "", "700000", "Error: Last Name is required" },
                { "Nguyen", "Van A", "", "Error: Postal Code is required" },
        };
    }
}
