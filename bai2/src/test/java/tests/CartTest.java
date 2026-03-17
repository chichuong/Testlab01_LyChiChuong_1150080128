package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import utils.ConfigReader;

public class CartTest extends BaseTest {

    @Test
    public void testAddAnItemToCart() {
        getDriver().get(ConfigReader.getProperty("url"));
        
        // Triển khai Fluent Interface: loginPage.login(u,p).addFirstItemToCart().goToCart()
        CartPage cartPage = new LoginPage(getDriver())
                .login(ConfigReader.getProperty("username"), ConfigReader.getProperty("password"))
                .addFirstItemToCart()
                .goToCart();
                
        Assert.assertEquals(cartPage.getItemCount(), 1, "Giỏ hàng phải có 1 sản phẩm đã Add.");
    }

    @Test
    public void testRemoveItemFromCart() {
        getDriver().get(ConfigReader.getProperty("url"));
        
        // Triển khai Fluent Interface với remove
        CartPage cartPage = new LoginPage(getDriver())
                .login(ConfigReader.getProperty("username"), ConfigReader.getProperty("password"))
                .addFirstItemToCart()
                .goToCart()
                .removeFirstItem();
                
        Assert.assertEquals(cartPage.getItemCount(), 0, "Giỏ hàng phải rỗng sau khi Remove Item.");
    }

    @Test
    public void testEmptyCartItemCount() {
        getDriver().get(ConfigReader.getProperty("url"));
        
        CartPage cartPage = new LoginPage(getDriver())
                .login(ConfigReader.getProperty("username"), ConfigReader.getProperty("password"))
                .goToCart(); // Chuyển thẳng đến giỏ hàng
                
        // Yêu cầu "Xử lý trường hợp CartPage không có item - getItemCount() trả về 0, không throw exception"
        int cartItems = cartPage.getItemCount();
        Assert.assertEquals(cartItems, 0, "Giỏ hàng trống nhưng getItemCount không trả về 0.");
    }
}
