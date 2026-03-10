package dtm.base;

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
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * BaseTest - Lớp cơ sở cho tất cả test class.
 * Sử dụng ThreadLocal<WebDriver> để hỗ trợ parallel execution an toàn.
 */
public abstract class BaseTest {

    // ThreadLocal để hỗ trợ parallel execution
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @BeforeMethod
    public void setUp(Method method) {
        // Khởi tạo ChromeDriver qua WebDriverManager
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Bỏ comment nếu muốn chạy headless
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        // Maximize window
        driver.manage().window().maximize();

        // Set implicit wait 10s
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Lưu driver vào ThreadLocal
        driverThreadLocal.set(driver);

        // Ghi log tên test đang chạy
        System.out.println(">>> [START] " + method.getDeclaringClass().getSimpleName()
                + "." + method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = driverThreadLocal.get();

        // Nếu result là FAILURE → chụp screenshot lưu vào /screenshots/
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                // Tạo thư mục screenshots nếu chưa có
                Path screenshotDir = Paths.get("screenshots");
                if (!Files.exists(screenshotDir)) {
                    Files.createDirectories(screenshotDir);
                }

                // Chụp screenshot
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String fileName = result.getMethod().getMethodName() + "_"
                        + System.currentTimeMillis() + ".png";
                Path destPath = screenshotDir.resolve(fileName);
                Files.copy(srcFile.toPath(), destPath);

                System.out.println(">>> [SCREENSHOT] Saved: " + destPath.toAbsolutePath());
            } catch (IOException e) {
                System.out.println(">>> [ERROR] Cannot save screenshot: " + e.getMessage());
            }
        }

        // Log kết quả
        String status = result.getStatus() == ITestResult.SUCCESS ? "PASSED" :
                result.getStatus() == ITestResult.FAILURE ? "FAILED" : "SKIPPED";
        System.out.println(">>> [END] " + result.getMethod().getMethodName() + " - " + status);

        // Đóng driver, remove ThreadLocal
        if (driver != null) {
            driver.quit();
        }
        driverThreadLocal.remove();
    }

    /**
     * Trả về driver của thread hiện tại.
     */
    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }
}
