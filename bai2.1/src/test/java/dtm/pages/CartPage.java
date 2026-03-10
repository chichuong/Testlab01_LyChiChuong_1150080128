package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object - Trang giỏ hàng (https://www.saucedemo.com/cart.html)
 */
public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Kiểm tra đang ở trang Cart
     */
    public boolean isOnCartPage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(lblTitle));
            return lblTitle.getText().equals("Your Cart");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy số lượng sản phẩm trong giỏ
     */
    public int getCartItemCount() {
        return cartItems.size();
    }

    /**
     * Lấy danh sách tên sản phẩm trong giỏ
     */
    public List<String> getCartItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(By.className("inventory_item_name")).getText())
                .collect(Collectors.toList());
    }

    /**
     * Lấy giá sản phẩm theo tên
     */
    public double getItemPrice(String productName) {
        for (WebElement item : cartItems) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                String priceText = item.findElement(By.className("inventory_item_price")).getText();
                return Double.parseDouble(priceText.replace("$", ""));
            }
        }
        return -1;
    }

    /**
     * Xóa sản phẩm khỏi giỏ theo tên
     */
    public CartPage removeItemByName(String productName) {
        for (WebElement item : cartItems) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.cssSelector("button[id^='remove']")).click();
                break;
            }
        }
        // Refresh lại element sau khi xóa
        PageFactory.initElements(driver, this);
        return this;
    }

    /**
     * Click Checkout
     */
    public void clickCheckout() {
        btnCheckout.click();
    }

    /**
     * Click Continue Shopping
     */
    public void clickContinueShopping() {
        btnContinueShopping.click();
    }
}
