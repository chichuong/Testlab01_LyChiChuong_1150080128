package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object cho trang san pham /inventory.html
 */
public class InventoryPage {

    private WebDriver driver;

    // ===== @FindBy =====

    @FindBy(css = "[data-test='product-sort-container']")
    private WebElement sortDropdown;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = "button[id^='add-to-cart']")
    private List<WebElement> addToCartButtons;

    @FindBy(css = "button[id^='remove']")
    private List<WebElement> removeButtons;

    @FindBy(css = ".shopping_cart_badge")
    private List<WebElement> cartBadgeElements;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement hamburgerMenu;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateLink;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;

    // ===== Constructor =====
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ===== Them san pham theo ten =====
    public void themSanPhamTheoTen(String tenSanPham) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText();
            if (name.equalsIgnoreCase(tenSanPham)) {
                item.findElement(By.cssSelector("button[id^='add-to-cart']")).click();
                return;
            }
        }
        throw new RuntimeException("Khong tim thay san pham: " + tenSanPham);
    }

    // ===== Them N san pham dau tien =====
    public void themNSanPhamDauTien(int n) {
        int count = Math.min(n, addToCartButtons.size());
        for (int i = 0; i < count; i++) {
            // Re-find buttons each time because DOM changes after clicking
            List<WebElement> buttons = driver.findElements(By.cssSelector("button[id^='add-to-cart']"));
            if (!buttons.isEmpty()) {
                buttons.get(0).click();
            }
        }
    }

    // ===== Lay so luong badge gio hang =====
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

    // ===== Sort san pham =====
    public void sortSanPham(String option) {
        Select select = new Select(sortDropdown);
        switch (option.toLowerCase()) {
            case "az":
                select.selectByValue("az");
                break;
            case "za":
                select.selectByValue("za");
                break;
            case "lohi":
                select.selectByValue("lohi");
                break;
            case "hilo":
                select.selectByValue("hilo");
                break;
            default:
                select.selectByValue(option);
        }
    }

    // ===== Lay danh sach ten san pham theo thu tu hien thi =====
    public List<String> layDanhSachTenSanPham() {
        List<String> names = new ArrayList<>();
        for (WebElement el : productNames) {
            names.add(el.getText());
        }
        return names;
    }

    // ===== Lay danh sach gia san pham theo thu tu hien thi =====
    public List<Double> layDanhSachGiaSanPham() {
        List<Double> prices = new ArrayList<>();
        for (WebElement el : productPrices) {
            String priceText = el.getText().replace("$", "");
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }

    // ===== Lay tong so san pham tren trang =====
    public int laySoLuongSanPham() {
        return inventoryItems.size();
    }

    // ===== Click vao gio hang =====
    public void clickGioHang() {
        cartLink.click();
    }

    // ===== Xoa san pham theo ten =====
    public void xoaSanPhamTheoTen(String tenSanPham) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText();
            if (name.equalsIgnoreCase(tenSanPham)) {
                item.findElement(By.cssSelector("button[id^='remove']")).click();
                return;
            }
        }
    }

    // ===== Lay so nut Remove hien thi =====
    public int laySoNutRemove() {
        return removeButtons.size();
    }

    // ===== Kiem tra dang o trang inventory =====
    public boolean isDangOTrangSanPham() {
        return driver.getCurrentUrl().contains("inventory.html");
    }

    // ===== Logout =====
    public void logout() {
        hamburgerMenu.click();
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        logoutLink.click();
    }

    // ===== Reset App State =====
    public void resetAppState() {
        hamburgerMenu.click();
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        resetAppStateLink.click();
    }

    // ===== Lay gia san pham theo ten =====
    public double layGiaSanPhamTheoTen(String tenSanPham) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText();
            if (name.equalsIgnoreCase(tenSanPham)) {
                String priceText = item.findElement(By.cssSelector(".inventory_item_price")).getText().replace("$", "");
                return Double.parseDouble(priceText);
            }
        }
        throw new RuntimeException("Khong tim thay san pham: " + tenSanPham);
    }
}
