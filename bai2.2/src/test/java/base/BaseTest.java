package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        // Tat password manager va leak detection de tranh popup
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("autofill.profile_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        // Tat infobars "Chrome is being controlled..."
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driverThreadLocal.set(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                // Chup anh man hinh khi test that bai
                if (result.getStatus() == ITestResult.FAILURE) {
                    chupAnhManHinh(result.getName());
                }
            } catch (Exception e) {
                // Ignore - driver co the da crash
            } finally {
                try {
                    driver.quit();
                } catch (Exception e) {
                    // Ignore
                }
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Chụp ảnh màn hình và lưu vào thư mục screenshots
     */
    protected void chupAnhManHinh(String tenTest) {
        try {
            WebDriver driver = getDriver();
            if (driver instanceof TakesScreenshot) {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                Path destDir = Paths.get("screenshots");
                Files.createDirectories(destDir);
                Path destFile = destDir.resolve(tenTest + "_" + timestamp + ".png");
                Files.copy(srcFile.toPath(), destFile);
                System.out.println("Screenshot saved: " + destFile.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Khong the chup anh man hinh: " + e.getMessage());
        }
    }
}
