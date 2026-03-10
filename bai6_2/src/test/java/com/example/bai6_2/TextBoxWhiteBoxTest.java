package com.example.bai6_2;

import com.example.bai6_2.pages.TextBoxPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * White-box test cho trang https://demoqa.com/text-box
 *
 * Các test case được xây dựng dựa trên CFG và Boundary Value Analysis:
 *
 * ===== CONTROL FLOW GRAPH (CFG) =====
 *
 * ┌─────────────────────────┐
 * │ [1] START: Page loaded │
 * └────────┬────────────────┘
 * ▼
 * ┌─────────────────────────┐
 * │ [2] Nhập Full Name │
 * └────────┬────────────────┘
 * ▼
 * ┌─────────────────────────┐
 * │ [3] Nhập Email │
 * └────────┬────────────────┘
 * ▼
 * ┌─────────────────────────┐
 * │ [4] Nhập Current Addr │
 * └────────┬────────────────┘
 * ▼
 * ┌─────────────────────────┐
 * │ [5] Nhập Permanent Addr │
 * └────────┬────────────────┘
 * ▼
 * ┌─────────────────────────┐
 * │ [6] Click Submit │
 * └────────┬────────────────┘
 * ▼
 * ┌──────────────────────────────────────────┐
 * │ [7] JS Validate Email │
 * │ regex: /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/ │
 * └────────┬─────────────────────────────────┘
 * ▼
 * ┌──────────────────────────┐
 * │ [8] Email hợp lệ hoặc │
 * │ rỗng? │
 * └───┬─────────────┬───────┘
 * │ YES │ NO
 * ▼ ▼
 * ┌──────────┐ ┌───────────────────────┐
 * │[9] Hiển │ │[10] Thêm class │
 * │thị output│ │"field-error" vào email │
 * │section │ │KHÔNG hiển thị output │
 * └────┬─────┘ └──────────┬────────────┘
 * │ │
 * ▼ ▼
 * ┌─────────────────────────┐
 * │ [11] END │
 * └─────────────────────────┘
 *
 * ===== BOUNDARY VALUE ANALYSIS =====
 *
 * Field | Boundary Cases
 * ───────────────┼──────────────────────────────────────────
 * Full Name | "", " ", "!@#$%^&*()", "A".repeat(256), "Nguyen Van A"
 * Email | "", "test@example.com", "test", "@example.com",
 * | "test@", "test @example.com", "!@#@example.com",
 * | "test@example", "a@b.cd" (min domain), " "
 * Current Addr | "", " ", "<script>alert(1)</script>", "Line1\nLine2",
 * "A".repeat(500)
 * Permanent Addr | "", " ", "!@#$%", "A".repeat(500), "123 Main St"
 *
 * Paths trong CFG:
 * - Path 1: [1]-[2]-[3]-[4]-[5]-[6]-[7]-[8]-YES-[9]-[11] (Happy path)
 * - Path 2: [1]-[2]-[3]-[4]-[5]-[6]-[7]-[8]-NO-[10]-[11] (Invalid email)
 */
public class TextBoxWhiteBoxTest {

    private static WebDriver driver;
    private TextBoxPage textBoxPage;

    @BeforeAll
    static void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        textBoxPage = new TextBoxPage(driver);
        textBoxPage.open();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // =====================================================
    // PATH 1: Happy Path - Tất cả dữ liệu hợp lệ
    // CFG: [1]→[2]→[3]→[4]→[5]→[6]→[7]→[8]→YES→[9]→[11]
    // =====================================================

    @Test
    @DisplayName("TC01 - Happy path: Nhập đầy đủ thông tin hợp lệ → hiển thị output")
    void testHappyPath_AllValidInputs() {
        textBoxPage.fillAndSubmitAll("Nguyen Van A", "test@example.com", "123 Le Loi, Q1", "456 Tran Hung Dao, Q5");

        assertTrue(textBoxPage.isOutputDisplayed(), "Output section phải hiển thị");
        assertEquals("Nguyen Van A", textBoxPage.getOutputName());
        assertEquals("test@example.com", textBoxPage.getOutputEmail());
        assertFalse(textBoxPage.hasEmailFieldError(), "Không được có lỗi email");
    }

    @Test
    @DisplayName("TC02 - Chỉ nhập Name và Email hợp lệ, address rỗng → hiển thị output")
    void testValidNameAndEmail_EmptyAddresses() {
        textBoxPage.fillAndSubmitAll("Le Thi B", "le.thi.b@gmail.com", "", "");

        assertTrue(textBoxPage.isOutputDisplayed(), "Output section phải hiển thị");
        assertEquals("Le Thi B", textBoxPage.getOutputName());
        assertEquals("le.thi.b@gmail.com", textBoxPage.getOutputEmail());
    }

    // =====================================================
    // PATH 2: Invalid Email → field-error, không hiển thị output
    // CFG: [1]→[2]→[3]→[4]→[5]→[6]→[7]→[8]→NO→[10]→[11]
    // =====================================================

    @Test
    @DisplayName("TC03 - Email thiếu @ → field-error, không hiển thị output")
    void testInvalidEmail_MissingAtSign() {
        textBoxPage.fillAndSubmitAll("User Name", "testexample.com", "Address 1", "Address 2");

        assertTrue(textBoxPage.hasEmailFieldError(), "Email field phải có class field-error");
        assertFalse(textBoxPage.isOutputDisplayed(), "Output section không được hiển thị");
    }

    @Test
    @DisplayName("TC04 - Email thiếu domain → field-error")
    void testInvalidEmail_MissingDomain() {
        textBoxPage.fillAndSubmitAll("User Name", "test@", "Address 1", "");

        assertTrue(textBoxPage.hasEmailFieldError(), "Email field phải có class field-error");
        assertFalse(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC05 - Email thiếu username (chỉ có @domain) → field-error")
    void testInvalidEmail_MissingUsername() {
        textBoxPage.fillAndSubmitAll("User Name", "@example.com", "Address 1", "");

        assertTrue(textBoxPage.hasEmailFieldError(), "Email field phải có class field-error");
        assertFalse(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC06 - Email có khoảng trắng → field-error")
    void testInvalidEmail_WithSpaces() {
        textBoxPage.fillAndSubmitAll("User Name", "test @example.com", "Address 1", "");

        assertTrue(textBoxPage.hasEmailFieldError(), "Email có khoảng trắng phải bị lỗi");
        assertFalse(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC07 - Email chỉ có khoảng trắng → field-error")
    void testInvalidEmail_OnlySpaces() {
        textBoxPage.fillAndSubmitAll("User Name", "   ", "Address 1", "");

        assertTrue(textBoxPage.hasEmailFieldError(), "Email chỉ khoảng trắng phải bị lỗi");
        assertFalse(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC08 - Email có ký tự đặc biệt không hợp lệ → field-error")
    void testInvalidEmail_SpecialChars() {
        textBoxPage.fillAndSubmitAll("User Name", "!#$%@example.com", "Address 1", "");

        assertTrue(textBoxPage.hasEmailFieldError(), "Email ký tự đặc biệt phải bị lỗi");
        assertFalse(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC09 - Email thiếu TLD (top-level domain) → field-error")
    void testInvalidEmail_MissingTLD() {
        textBoxPage.fillAndSubmitAll("User Name", "test@example", "Address 1", "");

        assertTrue(textBoxPage.hasEmailFieldError(), "Email thiếu TLD phải bị lỗi");
        assertFalse(textBoxPage.isOutputDisplayed());
    }

    // =====================================================
    // BOUNDARY: Email rỗng → Hợp lệ (Path 1)
    // =====================================================

    @Test
    @DisplayName("TC10 - Email rỗng → không lỗi, hiển thị output (chỉ các field khác)")
    void testEmptyEmail_ShouldBeAccepted() {
        textBoxPage.fillAndSubmitAll("User Name", "", "My Address", "");

        assertFalse(textBoxPage.hasEmailFieldError(), "Email rỗng không được báo lỗi");
        assertTrue(textBoxPage.isOutputDisplayed(), "Output phải hiển thị khi email rỗng");
    }

    // =====================================================
    // BOUNDARY: Email hợp lệ - giá trị biên
    // =====================================================

    @Test
    @DisplayName("TC11 - Email hợp lệ với domain ngắn nhất (2 ký tự TLD) → thành công")
    void testValidEmail_MinTLD() {
        textBoxPage.fillAndSubmitAll("User", "a@b.cd", "Addr", "");

        assertFalse(textBoxPage.hasEmailFieldError());
        assertTrue(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC12 - Email hợp lệ với dấu chấm trong username → thành công")
    void testValidEmail_DotsInUsername() {
        textBoxPage.fillAndSubmitAll("User", "first.last@example.com", "Addr", "");

        assertFalse(textBoxPage.hasEmailFieldError());
        assertTrue(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC13 - Email hợp lệ với dấu gạch ngang → thành công")
    void testValidEmail_HyphenInUsername() {
        textBoxPage.fillAndSubmitAll("User", "first-last@example.com", "Addr", "");

        assertFalse(textBoxPage.hasEmailFieldError());
        assertTrue(textBoxPage.isOutputDisplayed());
    }

    // =====================================================
    // BOUNDARY: Full Name - các giá trị biên
    // =====================================================

    @Test
    @DisplayName("TC14 - Name rỗng → không lỗi, output hiển thị (không có dòng Name)")
    void testEmptyName() {
        textBoxPage.fillAndSubmitAll("", "test@example.com", "Address", "");

        assertFalse(textBoxPage.hasEmailFieldError());
        assertTrue(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC15 - Name chỉ có khoảng trắng → chấp nhận")
    void testName_OnlyWhitespace() {
        textBoxPage.fillAndSubmitAll("   ", "test@example.com", "Address", "");

        assertFalse(textBoxPage.hasEmailFieldError());
        assertTrue(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC16 - Name có ký tự đặc biệt → chấp nhận (không validate name)")
    void testName_SpecialCharacters() {
        textBoxPage.fillAndSubmitAll("!@#$%^&*()", "test@example.com", "Address", "");

        assertFalse(textBoxPage.hasEmailFieldError());
        assertTrue(textBoxPage.isOutputDisplayed());
        assertEquals("!@#$%^&*()", textBoxPage.getOutputName());
    }

    @Test
    @DisplayName("TC17 - Name rất dài (256 ký tự) → chấp nhận")
    void testName_VeryLongString() {
        String longName = "A".repeat(256);
        textBoxPage.fillAndSubmitAll(longName, "test@example.com", "Address", "");

        assertFalse(textBoxPage.hasEmailFieldError());
        assertTrue(textBoxPage.isOutputDisplayed());
    }

    // =====================================================
    // BOUNDARY: Current Address - các giá trị biên
    // =====================================================

    @Test
    @DisplayName("TC18 - Current Address rỗng → chấp nhận")
    void testCurrentAddress_Empty() {
        textBoxPage.fillAndSubmitAll("User", "test@example.com", "", "Perm Addr");

        assertTrue(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC19 - Current Address chỉ khoảng trắng → chấp nhận")
    void testCurrentAddress_OnlyWhitespace() {
        textBoxPage.fillAndSubmitAll("User", "test@example.com", "   ", "");

        assertTrue(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC20 - Current Address có ký tự đặc biệt → chấp nhận")
    void testCurrentAddress_SpecialChars() {
        textBoxPage.fillAndSubmitAll("User", "test@example.com", "<>&\"'!@#$%", "");

        assertTrue(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC21 - Current Address rất dài → chấp nhận")
    void testCurrentAddress_VeryLong() {
        String longAddr = "B".repeat(500);
        textBoxPage.fillAndSubmitAll("User", "test@example.com", longAddr, "");

        assertTrue(textBoxPage.isOutputDisplayed());
    }

    // =====================================================
    // BOUNDARY: Permanent Address - các giá trị biên
    // =====================================================

    @Test
    @DisplayName("TC22 - Permanent Address rỗng → chấp nhận")
    void testPermanentAddress_Empty() {
        textBoxPage.fillAndSubmitAll("User", "test@example.com", "Curr Addr", "");

        assertTrue(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC23 - Permanent Address có ký tự đặc biệt → chấp nhận")
    void testPermanentAddress_SpecialChars() {
        textBoxPage.fillAndSubmitAll("User", "test@example.com", "Curr", "!@#$%^&*()_+");

        assertTrue(textBoxPage.isOutputDisplayed());
    }

    // =====================================================
    // BOUNDARY: Tất cả field rỗng
    // =====================================================

    @Test
    @DisplayName("TC24 - Tất cả field rỗng → submit thành công, output rỗng hoặc không hiển thị")
    void testAllFieldsEmpty() {
        textBoxPage.fillAndSubmitAll("", "", "", "");

        assertFalse(textBoxPage.hasEmailFieldError(), "Không có lỗi email khi tất cả rỗng");
    }

    // =====================================================
    // BOUNDARY: Email đúng format nhưng ở ranh giới
    // =====================================================

    @Test
    @DisplayName("TC25 - Email có 2 dấu @ liên tiếp → field-error")
    void testInvalidEmail_DoubleAtSign() {
        textBoxPage.fillAndSubmitAll("User", "test@@example.com", "Addr", "");

        assertTrue(textBoxPage.hasEmailFieldError());
        assertFalse(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC26 - Email có dấu chấm ở cuối domain → field-error")
    void testInvalidEmail_TrailingDot() {
        textBoxPage.fillAndSubmitAll("User", "test@example.com.", "Addr", "");

        assertTrue(textBoxPage.hasEmailFieldError());
        assertFalse(textBoxPage.isOutputDisplayed());
    }

    @Test
    @DisplayName("TC27 - Email TLD quá dài (>4 ký tự) → field-error theo regex")
    void testInvalidEmail_TLDTooLong() {
        textBoxPage.fillAndSubmitAll("User", "test@example.abcde", "Addr", "");

        assertTrue(textBoxPage.hasEmailFieldError(), "TLD > 4 ký tự phải bị lỗi theo regex");
        assertFalse(textBoxPage.isOutputDisplayed());
    }
}
