package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(className = "inventory_list")
    private WebElement inventoryList;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(xpath = "(//button[contains(@class, 'btn_inventory')])[1]")
    private WebElement firstAddToCartBtn;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(inventoryList);
    }

    public InventoryPage addFirstItemToCart() {
        waitAndClick(firstAddToCartBtn);
        return this; // Áp dụng quy tắc Fluent Interface
    }

    public InventoryPage addItemByName(String name) {
        // Tương tác động không thể dùng @FindBy, nên sử dụng By Locators chung kèm BasePage call
        By itemBtn = By.xpath("//div[div/a/div[normalize-space(text())='" + name + "']]//button");
        waitAndClick(itemBtn);
        return this;
    }

    public int getCartItemCount() {
        try {
            if (isElementVisible(cartBadge)) {
                return Integer.parseInt(getText(cartBadge));
            }
        } catch (Exception e) {
            // Trường hợp không có badge (giỏ trống)
            return 0;
        }
        return 0;
    }

    public CartPage goToCart() {
        waitAndClick(cartLink);
        waitForPageLoad();
        return new CartPage(driver);
    }
}
