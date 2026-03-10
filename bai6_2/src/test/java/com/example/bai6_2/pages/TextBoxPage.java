package com.example.bai6_2.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * Page Object Model cho trang https://demoqa.com/text-box
 *
 * CFG (Control Flow Graph) - Luồng xử lý form:
 *
 * [1] START: Page loaded
 * │
 * [2] Nhập Full Name (id="userName")
 * │
 * [3] Nhập Email (id="userEmail")
 * │
 * [4] Nhập Current Address (id="currentAddress")
 * │
 * [5] Nhập Permanent Address (id="permanentAddress")
 * │
 * [6] Click Submit
 * │
 * [7] Validate Email (JS regex: /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/)
 * │
 * [8] Email hợp lệ HOẶC rỗng?
 * / \
 * YES NO
 * │ │
 * [9] [10] Thêm class "field-error" vào email field
 * │ │ Không hiển thị output
 * │ │
 * [11] Hiển thị output section (#output)
 * │ với các giá trị đã nhập (chỉ hiện field không rỗng)
 * │
 * [12] END
 *
 * Boundary Values được xác định từ CFG:
 * - Full Name: empty(""), whitespace(" "), ký tự đặc biệt("@#$%"), chuỗi
 * dài(256+ chars), giá trị bình thường
 * - Email: empty(""), valid("test@example.com"), invalid(thiếu @, thiếu domain,
 * chỉ whitespace, ký tự đặc biệt)
 * - Current Address: empty(""), whitespace, ký tự đặc biệt, multiline, chuỗi
 * dài
 * - Permanent Address: empty(""), whitespace, ký tự đặc biệt, chuỗi dài
 */
public class TextBoxPage {

    private WebDriver driver;

    @FindBy(id = "userName")
    private WebElement nameField;

    @FindBy(id = "userEmail")
    private WebElement emailField;

    @FindBy(id = "currentAddress")
    private WebElement currentAddressField;

    @FindBy(id = "permanentAddress")
    private WebElement permanentAddressField;

    @FindBy(id = "submit")
    private WebElement submitBtn;

    @FindBy(id = "output")
    private WebElement outputSection;

    @FindBy(id = "name")
    private WebElement outputName;

    @FindBy(id = "email")
    private WebElement outputEmail;

    @FindBy(css = "#output #currentAddress")
    private WebElement outputCurrentAddress;

    @FindBy(css = "#output #permanentAddress")
    private WebElement outputPermanentAddress;

    @FindBy(css = ".mr-sm-2.field-error")
    private List<WebElement> fieldErrors;

    public TextBoxPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get("https://demoqa.com/text-box");
    }

    public void fillAndSubmit(String name, String email, String address) {
        fillAndSubmitAll(name, email, address, "");
    }

    public void fillAndSubmitAll(String name, String email, String currentAddress, String permanentAddress) {
        nameField.clear();
        nameField.sendKeys(name);
        emailField.clear();
        emailField.sendKeys(email);
        currentAddressField.clear();
        currentAddressField.sendKeys(currentAddress);
        permanentAddressField.clear();
        permanentAddressField.sendKeys(permanentAddress);

        // Scroll to submit button để tránh bị che bởi footer
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        submitBtn.click();
    }

    public boolean isOutputDisplayed() {
        try {
            return outputSection.isDisplayed() && !outputSection.getText().isEmpty();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public String getOutputName() {
        try {
            return outputName.getText().replace("Name:", "").trim();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return "";
        }
    }

    public String getOutputEmail() {
        try {
            return outputEmail.getText().replace("Email:", "").trim();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return "";
        }
    }

    public String getOutputCurrentAddress() {
        try {
            return outputCurrentAddress.getText().replace("Current Address :", "").trim();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return "";
        }
    }

    public String getOutputPermanentAddress() {
        try {
            return outputPermanentAddress.getText().replace("Permananet Address :", "").trim();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return "";
        }
    }

    public boolean hasEmailFieldError() {
        return !fieldErrors.isEmpty();
    }

    public String getEmailFieldClass() {
        return emailField.getAttribute("class");
    }
}
