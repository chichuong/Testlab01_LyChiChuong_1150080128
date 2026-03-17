package tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ExcelReader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginTest extends BaseTest implements ITest {

    private ThreadLocal<String> testName = new ThreadLocal<>();

    @Override
    public String getTestName() {
        return testName.get() != null ? testName.get() : "Login cases";
    }

    @BeforeMethod(alwaysRun = true)
    public void setTestName(Method method, Object[] testData) {
        if (testData != null && testData.length > 0) {
            // Description is always the last argument (index 3)
            String description = (String) testData[3];
            testName.set(method.getName() + " - " + description);
        }
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData(ITestContext context) {
        String[] groups = context.getIncludedGroups();
        boolean isSmoke = false;
        boolean isRegression = false;

        for (String group : groups) {
            if ("smoke".equalsIgnoreCase(group)) isSmoke = true;
            if ("regression".equalsIgnoreCase(group)) isRegression = true;
        }

        List<Object[]> allData = new ArrayList<>();
        String filePath = "src/test/resources/login_data.xlsx";

        if (isSmoke) {
            allData.addAll(readSheet(filePath, "SmokeCases", "valid"));
        } else if (isRegression) {
            allData.addAll(readSheet(filePath, "SmokeCases", "valid"));
            allData.addAll(readSheet(filePath, "NegativeCases", "invalid"));
            allData.addAll(readSheet(filePath, "BoundaryCases", "invalid"));
        } else {
            // Default run all if no group specified
            allData.addAll(readSheet(filePath, "SmokeCases", "valid"));
            allData.addAll(readSheet(filePath, "NegativeCases", "invalid"));
            allData.addAll(readSheet(filePath, "BoundaryCases", "invalid"));
        }

        return allData.toArray(new Object[0][0]);
    }

    private List<Object[]> readSheet(String filePath, String sheetName, String type) {
        Object[][] rawData = ExcelReader.getExcelData(filePath, sheetName);
        List<Object[]> formattedData = new ArrayList<>();
        // Skip header at row 0 when adding to list
        for (int i = 0; i < rawData.length; i++) {
            // rawData already skipped header during ExcelReader generation or reading? 
            // Wait, my ExcelReader starts reading from i=1 so it ALREADY skips row 0 (header)!
            // Expected columns: username, password, expected_value, description
            String username = (String) rawData[i][0];
            String password = (String) rawData[i][1];
            String expected = (String) rawData[i][2];
            String description = (String) rawData[i][3];
            formattedData.add(new Object[]{username, password, expected, description, type});
        }
        return formattedData;
    }

    // Using "groups" on @Test is required so TestNG sees it
    @Test(dataProvider = "loginData", groups = {"smoke", "regression"})
    public void testLoginFromExcel(String username, String password, String expectedValue, String description, String type) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        if ("valid".equals(type)) {
            // Expect redirect to expected URL
            String currentUrl = getDriver().getCurrentUrl();
            Assert.assertEquals(currentUrl, expectedValue, "URL does not match expected for valid login");
        } else {
            // Expect error message
            String actualError = loginPage.getErrorMessage();
            Assert.assertEquals(actualError, expectedValue, "Error message does not match expected for invalid login");
        }
    }
}
