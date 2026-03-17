package pages;

import core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(xpath = "(//button[contains(text(), 'Remove')])[1]")
    private WebElement firstRemoveBtn;

    @FindBy(id = "checkout")
    private WebElement checkoutBtn;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        // Xử lý yêu cầu: "trường hợp CartPage không có item - getItemCount() trả về 0, không throw exception"
        try {
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage removeFirstItem() {
        if (!cartItems.isEmpty()) {
            waitAndClick(firstRemoveBtn);
        }
        return this;
    }

    public CheckoutPage goToCheckout() {
        waitAndClick(checkoutBtn);
        return new CheckoutPage(driver);
    }

    public List<String> getItemNames() {
        return itemNames.stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());
    }
}
