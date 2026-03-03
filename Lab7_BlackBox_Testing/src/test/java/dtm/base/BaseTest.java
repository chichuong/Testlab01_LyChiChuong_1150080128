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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * BaseTest - Lớp cơ sở cho tất cả các test class.
 * Quản lý WebDriver bằng ThreadLocal để hỗ trợ parallel execution.
 */
public abstract class BaseTest {

    // ThreadLocal<WebDriver> để hỗ trợ parallel execution
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
                + "." + method.getName()
                + " | Thread: " + Thread.currentThread().getId());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = driverThreadLocal.get();

        // Nếu result là FAILURE → chụp screenshot lưu vào /screenshots/
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                captureScreenshot(driver, result.getName());
            } catch (Exception e) {
                System.err.println("Không thể chụp screenshot: " + e.getMessage());
            }
        }

        System.out.println("<<< [END] " + result.getMethod().getMethodName()
                + " | Status: " + getStatusName(result.getStatus()));

        // Đóng driver
        if (driver != null) {
            driver.quit();
        }

        // Remove ThreadLocal
        driverThreadLocal.remove();
    }

    /**
     * Trả về driver của thread hiện tại.
     */
    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Chụp screenshot và lưu vào thư mục screenshots/
     */
    private void captureScreenshot(WebDriver driver, String testName) {
        if (driver instanceof TakesScreenshot) {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";

            Path screenshotDir = Paths.get("screenshots");
            try {
                Files.createDirectories(screenshotDir);
                Path destination = screenshotDir.resolve(fileName);
                Files.copy(srcFile.toPath(), destination);
                System.out.println("Screenshot saved: " + destination.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Lỗi lưu screenshot: " + e.getMessage());
            }
        }
    }

    private String getStatusName(int status) {
        return switch (status) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
}
