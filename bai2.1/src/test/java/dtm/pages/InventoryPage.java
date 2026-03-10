package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object - Trang sản phẩm / Inventory (https://www.saucedemo.com/inventory.html)
 */
public class InventoryPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(className = "title")
    private WebElement lblTitle;

    @FindBy(className = "product_sort_container")
    private WebElement ddlSort;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "shopping_cart_link")
    private WebElement btnCart;

    @FindBy(className = "shopping_cart_badge")
    private WebElement lblCartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement btnMenu;

    @FindBy(id = "logout_sidebar_link")
    private WebElement btnLogout;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Kiểm tra đang ở trang Inventory
     */
    public boolean isOnInventoryPage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(lblTitle));
            return lblTitle.getText().equals("Products");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy tiêu đề trang
     */
    public String getPageTitle() {
        return lblTitle.getText();
    }

    /**
     * Lấy số lượng sản phẩm hiển thị
     */
    public int getProductCount() {
        return inventoryItems.size();
    }

    /**
     * Lấy danh sách tên sản phẩm
     */
    public List<String> getProductNames() {
        return inventoryItems.stream()
                .map(item -> item.findElement(By.className("inventory_item_name")).getText())
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách giá sản phẩm
     */
    public List<Double> getProductPrices() {
        return inventoryItems.stream()
                .map(item -> {
                    String priceText = item.findElement(By.className("inventory_item_price")).getText();
                    return Double.parseDouble(priceText.replace("$", ""));
                })
                .collect(Collectors.toList());
    }

    /**
     * Thêm sản phẩm vào giỏ hàng theo tên
     */
    public InventoryPage addToCartByName(String productName) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.cssSelector("button[id^='add-to-cart']")).click();
                break;
            }
        }
        return this;
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng theo tên (từ trang inventory)
     */
    public InventoryPage removeFromCartByName(String productName) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.cssSelector("button[id^='remove']")).click();
                break;
            }
        }
        return this;
    }

    /**
     * Lấy số trên badge giỏ hàng
     */
    public int getCartBadgeCount() {
        try {
            return Integer.parseInt(lblCartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Click vào giỏ hàng
     */
    public void goToCart() {
        btnCart.click();
    }

    /**
     * Sắp xếp sản phẩm
     * Giá trị: "az", "za", "lohi", "hilo"
     */
    public InventoryPage sortBy(String value) {
        Select select = new Select(ddlSort);
        select.selectByValue(value);
        return this;
    }

    /**
     * Logout
     */
    public void logout() {
        btnMenu.click();
        wait.until(ExpectedConditions.elementToBeClickable(btnLogout));
        btnLogout.click();
    }
}
