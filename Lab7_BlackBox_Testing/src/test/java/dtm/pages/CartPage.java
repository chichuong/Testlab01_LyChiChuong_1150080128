package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * Page Object - Trang giỏ hàng (https://www.saucedemo.com/cart.html)
 */
public class CartPage {

    private final WebDriver driver;

    @FindBy(className = "title")
    private WebElement lblTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement btnCheckout;

    @FindBy(id = "continue-shopping")
    private WebElement btnContinueShopping;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Kiểm tra đang ở trang Cart.
     */
    public boolean isOnCartPage() {
        try {
            return lblTitle.isDisplayed() && lblTitle.getText().equals("Your Cart");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy số sản phẩm trong giỏ hàng.
     */
    public int getCartItemCount() {
        return cartItems.size();
    }

    /**
     * Lấy danh sách tên sản phẩm trong giỏ.
     */
    public List<String> getCartItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(By.className("inventory_item_name")).getText())
                .toList();
    }

    /**
     * Kiểm tra sản phẩm có trong giỏ hàng không.
     */
    public boolean isProductInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    /**
     * Xoá sản phẩm khỏi giỏ hàng theo tên.
     */
    public CartPage removeProduct(String productName) {
        for (WebElement item : cartItems) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.cssSelector("button[id^='remove']")).click();
                break;
            }
        }
        return this;
    }

    /**
     * Click Checkout.
     */
    public void clickCheckout() {
        btnCheckout.click();
    }

    /**
     * Click Continue Shopping.
     */
    public void clickContinueShopping() {
        btnContinueShopping.click();
    }

    /**
     * Kiểm tra nút Checkout có hiển thị không.
     */
    public boolean isCheckoutEnabled() {
        try {
            return btnCheckout.isDisplayed() && btnCheckout.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
}
