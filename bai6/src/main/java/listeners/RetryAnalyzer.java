package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.ConfigReader;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private static final int maxRetry;

    static {
        String retryCountStr = ConfigReader.getProperty("retry.count");
        maxRetry = (retryCountStr != null) ? Integer.parseInt(retryCountStr) : 0;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (count < maxRetry) {
            count++;
            return true;
        }
        return false;
    }
}
