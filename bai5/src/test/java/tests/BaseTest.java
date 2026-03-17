package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

import java.time.Duration;

public class BaseTest {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        // BaseTest.setUp() gọi System.setProperty("env", env) trước khi khởi tạo ConfigReader
        String env = System.getProperty("env", "dev");
        System.setProperty("env", env);

        ConfigReader config = ConfigReader.getInstance();
        
        int explicitWait = Integer.parseInt(config.getProperty("explicit.wait"));
        System.out.println("explicit wait = " + explicitWait);

        String browser = config.getProperty("browser");
        WebDriver driver;

        if (browser == null) browser = "chrome";

        switch (browser.toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                driver = new ChromeDriver();
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        driver.get(config.getProperty("url"));
        driverThreadLocal.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }
}
