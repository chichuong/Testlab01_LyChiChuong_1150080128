package tests;

import models.UserData;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.JsonReader;

import java.lang.reflect.Method;
import java.util.List;

public class UserLoginTest extends BaseTest implements ITest {

    private ThreadLocal<String> testName = new ThreadLocal<>();

    @Override
    public String getTestName() {
        return testName.get() != null ? testName.get() : "User Login Test";
    }

    @BeforeMethod(alwaysRun = true)
    public void setTestName(Method method, Object[] testData) {
        if (testData != null && testData.length > 0) {
            UserData user = (UserData) testData[0];
            testName.set(method.getName() + " - " + user.getDescription());
        }
    }

    @DataProvider(name = "userData")
    public Object[][] getUserData() {
        List<UserData> users = JsonReader.readUsers("src/test/resources/users.json");
        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i);
        }
        return data;
    }

    @Test(dataProvider = "userData")
    public void testLoginWithJson(UserData user) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(user.getUsername(), user.getPassword());

        if (user.isExpectSuccess()) {
            String currentUrl = getDriver().getCurrentUrl();
            Assert.assertEquals(currentUrl, "https://practicetestautomation.com/logged-in-successfully/", "URL mismatch for successful login.");
        } else {
            String errorMessage = loginPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("invalid") || errorMessage.contains("username") || errorMessage.contains("password"),
                    "Error message not shown correctly: " + errorMessage);
        }
    }
}
