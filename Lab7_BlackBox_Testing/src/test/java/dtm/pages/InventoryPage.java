package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object - Trang sản phẩm / Inventory
 * (https://www.saucedemo.com/inventory.html)
 */
public class InventoryPage {

    private WebDriver driver;

    // @FindBy cho: sortDropdown, addToCartButtons (List),
    // removeButtons (List), cartBadge, cartLink
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(css = "button[id^='add-to-cart']")
    private List<WebElement> addToCartButtons;

    @FindBy(css = "button[id^='remove']")
    private List<WebElement> removeButtons;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(className = "title")
    private WebElement lblTitle;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /** Kiểm tra đang ở trang Inventory */
    public boolean isOnInventoryPage() {
        try {
            return lblTitle.isDisplayed() && lblTitle.getText().equals("Products");
        } catch (Exception e) {
            return false;
        }
    }

    /** Thêm sản phẩm theo tên */
    public void themSanPhamTheoTen(String tenSanPham) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(tenSanPham)) {
                item.findElement(By.cssSelector("button[id^='add-to-cart']")).click();
                return;
            }
        }
    }

    /** Thêm N sản phẩm đầu tiên trong danh sách */
    public void themNSanPhamDauTien(int n) {
        for (int i = 0; i < n; i++) {
            // Dùng driver.findElements trực tiếp để tránh lỗi proxy PageFactory
            List<WebElement> buttons = driver.findElements(By.cssSelector("button[id^='add-to-cart']"));
            if (!buttons.isEmpty()) {
                buttons.get(0).click();
            } else {
                break; // Không còn nút "Add to cart"
            }
        }
    }

    /** Trả về số lượng badge giỏ hàng, 0 nếu không có badge */
    public int laySoLuongBadge() {
        try {
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    /** Sort sản phẩm theo tuỳ chọn: 'az','za','lohi','hilo' */
    public void sortSanPham(String option) {
        Select select = new Select(sortDropdown);
        select.selectByValue(option);
    }

    /** Lấy danh sách tên sản phẩm theo thứ tự hiển thị */
    public List<String> layDanhSachTenSanPham() {
        PageFactory.initElements(driver, this);
        List<String> names = new ArrayList<>();
        for (WebElement el : productNames) {
            names.add(el.getText());
        }
        return names;
    }

    /** Lấy danh sách giá sản phẩm theo thứ tự hiển thị */
    public List<Double> layDanhSachGiaSanPham() {
        PageFactory.initElements(driver, this);
        List<Double> prices = new ArrayList<>();
        for (WebElement el : productPrices) {
            // Giá dạng "$29.99" → parse bỏ ký tự $
            String text = el.getText().replace("$", "");
            prices.add(Double.parseDouble(text));
        }
        return prices;
    }

    /** Xóa sản phẩm khỏi giỏ hàng theo tên (trên trang Inventory) */
    public void xoaSanPhamTheoTen(String tenSanPham) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(tenSanPham)) {
                item.findElement(By.cssSelector("button[id^='remove']")).click();
                return;
            }
        }
    }

    /** Xoá tất cả sản phẩm trong giỏ (trên trang Inventory) */
    public void xoaTatCaSanPham() {
        PageFactory.initElements(driver, this);
        while (!removeButtons.isEmpty()) {
            removeButtons.get(0).click();
            PageFactory.initElements(driver, this);
        }
    }

    /** Click vào giỏ hàng */
    public void goToCart() {
        cartLink.click();
    }

    /** Lấy số lượng sản phẩm trên trang */
    public int getProductCount() {
        return inventoryItems.size();
    }

    /** Đăng xuất */
    public void logout() {
        menuButton.click();
        logoutLink.click();
    }
}
