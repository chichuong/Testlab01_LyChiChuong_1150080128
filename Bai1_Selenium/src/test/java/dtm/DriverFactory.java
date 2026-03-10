package dtm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DriverFactory {
    // ThreadLocal: moi thread co bien driver rieng biet
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static void initDriver(String browser) {
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                // Tat Google Password Manager va cac popup lien quan
                options.addArguments("--disable-notifications");
                options.addArguments("--disable-popup-blocking");
                options.addArguments("--disable-save-password-bubble");
                options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
                // Tat password manager hoan toan
                java.util.Map<String, Object> prefs = new java.util.HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                options.setExperimentalOption("prefs", prefs);
                driver = new ChromeDriver(options);
        }
        driver.manage().window().maximize();
        tlDriver.set(driver);
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove(); // rat quan trong: tranh memory leak
        }
    }

    /**
     * Chup man hinh va luu vao thu muc screenshots/
     * @param fileName ten file (khong can .png)
     */
    public static void takeScreenshot(String fileName) {
        try {
            WebDriver driver = getDriver();
            if (driver == null) return;
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path screenshotDir = Paths.get("screenshots");
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }
            Path destPath = screenshotDir.resolve(fileName + ".png");
            Files.copy(srcFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[SCREENSHOT] Saved: " + destPath.toAbsolutePath());
        } catch (Exception e) {
            System.out.println("[SCREENSHOT] Failed: " + e.getMessage());
        }
    }

}
