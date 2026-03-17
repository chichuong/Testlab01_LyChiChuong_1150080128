package tests;

import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        // Base setup method if any configuration needs to run before tests
    }
}
