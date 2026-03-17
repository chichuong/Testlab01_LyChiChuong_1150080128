package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DemoTest extends BaseTest {

    @Test
    public void testEnvironmentConfig() {
        String currentUrl = getDriver().getCurrentUrl();
        System.out.println("Running test on URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("saucedemo"), "URL should contain saucedemo");
    }
}
