package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object cho trang gio hang /cart.html
 */
public class CartPage {

    private WebDriver driver;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(css = ".shopping_cart_badge")
    private List<WebElement> cartBadgeElements;

    // ===== Constructor =====
    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ===== Lay so luong item trong gio =====
    public int laySoLuongItem() {
        return cartItems.size();
    }

    // ===== Lay danh sach ten san pham trong gio =====
    public List<String> layDanhSachTenSanPham() {
        List<String> names = new ArrayList<>();
        for (WebElement item : cartItems) {
            names.add(item.findElement(By.cssSelector(".inventory_item_name")).getText());
        }
        return names;
    }

    // ===== Lay danh sach gia san pham trong gio =====
    public List<Double> layDanhSachGiaSanPham() {
        List<Double> prices = new ArrayList<>();
        for (WebElement item : cartItems) {
            String priceText = item.findElement(By.cssSelector(".inventory_item_price")).getText().replace("$", "");
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }

    // ===== Xoa san pham theo ten =====
    public void xoaSanPhamTheoTen(String tenSanPham) {
        for (WebElement item : cartItems) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText();
            if (name.equalsIgnoreCase(tenSanPham)) {
                item.findElement(By.cssSelector("button[id^='remove']")).click();
                return;
            }
        }
    }

    // ===== Xoa tat ca san pham trong gio =====
    public void xoaTatCaSanPham() {
        while (!cartItems.isEmpty()) {
            cartItems.get(0).findElement(By.cssSelector("button[id^='remove']")).click();
            // Re-init to refresh elements
            PageFactory.initElements(driver, this);
        }
    }

    // ===== Lay so luong badge =====
    public int laySoLuongBadge() {
        if (cartBadgeElements.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(cartBadgeElements.get(0).getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ===== Click Continue Shopping =====
    public void clickContinueShopping() {
        continueShoppingButton.click();
    }

    // ===== Click Checkout =====
    public void clickCheckout() {
        checkoutButton.click();
    }

    // ===== Kiem tra nut Checkout co hien thi =====
    public boolean isCheckoutButtonVisible() {
        try {
            return checkoutButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ===== Kiem tra dang o trang cart =====
    public boolean isDangOTrangCart() {
        return driver.getCurrentUrl().contains("cart.html");
    }
}
