package core;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BaseTest {

    // Sử dụng ThreadLocal để quản lý driver cho từng luồng (thread) chạy test song song.
    // KHÔNG dùng biến static để tránh conflict khi chạy parallel.
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    @BeforeMethod
    @Parameters({"browser", "env"})
    public void setUp(@Optional("chrome") String browser, @Optional("qa") String env) {
        WebDriver driver;
        System.out.println("Running test on browser: " + browser + " in environment: " + env);

        switch (browser.toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "chrome":
            default:
                ChromeOptions options = new ChromeOptions();
                // options.addArguments("--headless");
                driver = new ChromeDriver(options);
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Lưu driver riêng cho thread hiện tại
        threadLocalDriver.set(driver);
    }

    // Cung cấp WebDriver đễ cấp phát cho các Page Objects và Test class.
    public WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();
        if (driver != null) {
            // Chụp ảnh màn hình khi có test failed
            if (ITestResult.FAILURE == result.getStatus()) {
                takeScreenshot(driver, result.getMethod().getMethodName());
            }

            // Đóng trình duyệt sau khi kết thúc test
            driver.quit();
            // Xóa tham chiếu ThreadLocal để tránh rò rỉ bộ nhớ
            threadLocalDriver.remove();
        }
    }

    private void takeScreenshot(WebDriver driver, String testName) {
        if (driver instanceof TakesScreenshot) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotName = testName + "_" + timestamp + ".png";
            String destDirPath = "target/screenshots/";

            try {
                Path destDir = Paths.get(destDirPath);
                if (!Files.exists(destDir)) {
                    Files.createDirectories(destDir);
                }
                Path destPath = destDir.resolve(screenshotName);
                Files.copy(source.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Screenshot saved successfully to: " + destPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to save screenshot for test: " + testName + ". Error: " + e.getMessage());
            }
        }
    }
}
