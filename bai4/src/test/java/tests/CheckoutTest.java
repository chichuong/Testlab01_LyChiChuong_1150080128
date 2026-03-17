package tests;

import org.testng.annotations.Test;
import utils.TestDataFactory;

import java.util.Map;

public class CheckoutTest extends BaseTest {

    @Test(invocationCount = 2)
    public void testCheckoutWithRandomData() {
        Map<String, String> checkoutData = TestDataFactory.randomCheckoutData();

        System.out.println("=========================================");
        System.out.println("Running Checkout Test with Fake Data:");
        System.out.println("First Name: " + checkoutData.get("firstName"));
        System.out.println("Last Name: " + checkoutData.get("lastName"));
        System.out.println("Postal Code: " + checkoutData.get("postalCode"));
        System.out.println("=========================================");

        // Mọi @Test phải sử dụng getDriver() từ BaseTest - không tạo WebDriver mới trong test class
        getDriver(); // Lấy session WebDriver hiện tại để giả mạo quá trình tương tác (nếu có UI)
        
        // Mọi driver.findElement() phải nằm trong Page Object - không viết trực tiếp trong test.
        // Nơi đây sẽ gọi:
        // CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        // checkoutPage.enterCheckoutInfo(checkoutData.get("firstName"), ...);
    }
}
