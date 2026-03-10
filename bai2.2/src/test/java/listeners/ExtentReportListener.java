package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static int passCount = 0;
    private static int failCount = 0;
    private static int skipCount = 0;

    private static int finishCount = 0;
    private static int totalContexts = 0;

    @Override
    public void onStart(ITestContext context) {
        totalContexts++;
        if (extent == null) {
            passCount = 0;
            failCount = 0;
            skipCount = 0;

            ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Bai 2 - Kiem Thu SauceDemo");
            spark.config().setReportName("BAO CAO KIEM THU SAUCEDEMO - BAI 2.2 & 2.3");
            spark.config().setTimelineEnabled(true);
            spark.config().setEncoding("UTF-8");
            spark.config().setCss(
                ".summary-box { display: inline-block; padding: 15px 30px; margin: 8px; border-radius: 8px; color: white; font-size: 18px; font-weight: bold; text-align: center; min-width: 120px; box-shadow: 0 2px 8px rgba(0,0,0,0.15); }" +
                ".summary-total { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }" +
                ".summary-pass { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }" +
                ".summary-fail { background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); }" +
                ".summary-skip { background: linear-gradient(135deg, #f7971e 0%, #ffd200 100%); }" +
                ".summary-container { text-align: center; padding: 20px; margin-bottom: 20px; }" +
                ".summary-title { font-size: 22px; font-weight: bold; color: #2c3e50; margin-bottom: 15px; }" +
                ".badge-primary { background-color: #4723d9; }" +
                ".header { background-color: #2c3e50; }" +
                ".nav-bar { background-color: #34495e; }" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }" +
                ".test-content .test-steps { font-size: 14px; }" +
                ".test-name { font-weight: 600; font-size: 15px; }" +
                ".category-list .category { padding: 6px 12px; border-radius: 4px; color: white; font-weight: 500; }"
            );

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("He Dieu Hanh", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Website", "https://www.saucedemo.com");
            extent.setSystemInfo("Browser", "Google Chrome");
            extent.setSystemInfo("Mon hoc", "Kiem thu phan mem - Lab 7");
            extent.setSystemInfo("Bai tap", "Bai 2.2 (Dang Nhap) & Bai 2.3 (Gio Hang, Thanh Toan)");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        Object[] params = result.getParameters();

        // --- Truong hop 1: Test co DataProvider (Bai 2.2 - Dang Nhap) ---
        if (params.length >= 4 && params[3] != null) {
            testName = params[3].toString();
            ExtentTest test = extent.createTest(testName);

            String username = params[0] != null ? params[0].toString() : "null";
            String password = params[1] != null ? params[1].toString() : "null";
            String ketQua = params[2] != null ? params[2].toString() : "";

            test.info("<b>[BAI 2.2 - DANG NHAP]</b>");
            test.info("Username: <b>" + escapeHtml(username) + "</b>");
            test.info("Password: <b>" + escapeHtml(password) + "</b>");

            switch (ketQua) {
                case "THANH_CONG":
                    test.assignCategory("2.2 DANG NHAP THANH CONG");
                    test.info("Ket qua mong doi: <span style='color:green;font-weight:bold'>DANG NHAP THANH CONG</span>");
                    break;
                case "SAI_THONG_TIN":
                    test.assignCategory("2.2 SAI THONG TIN");
                    test.info("Ket qua mong doi: <span style='color:orange;font-weight:bold'>SAI THONG TIN DANG NHAP</span>");
                    break;
                case "BI_KHOA":
                    test.assignCategory("2.2 TAI KHOAN BI KHOA");
                    test.info("Ket qua mong doi: <span style='color:red;font-weight:bold'>TAI KHOAN BI KHOA</span>");
                    break;
                case "TRUONG_TRONG":
                    test.assignCategory("2.2 TRUONG TRONG");
                    test.info("Ket qua mong doi: <span style='color:#e67e22;font-weight:bold'>TRUONG BAT BUOC BO TRONG</span>");
                    break;
            }

            testThread.set(test);
            return;
        }

        // --- Truong hop 2: Test khong co DataProvider (Bai 2.3 - Gio Hang / Thanh Toan) ---
        if (description != null && !description.isEmpty()) {
            testName = description;
        }

        ExtentTest test = extent.createTest(testName);

        // Phan loai category theo ten test
        String methodName = result.getMethod().getMethodName().toLowerCase();

        if (methodName.contains("them") || methodName.contains("add")) {
            test.assignCategory("2.3 THEM SAN PHAM");
            test.info("<b>[BAI 2.3 - GIO HANG]</b> Kiem tra them san pham");
        } else if (methodName.contains("xoa") || methodName.contains("remove")) {
            test.assignCategory("2.3 XOA SAN PHAM");
            test.info("<b>[BAI 2.3 - GIO HANG]</b> Kiem tra xoa san pham");
        } else if (methodName.contains("sort") || methodName.contains("sap")) {
            test.assignCategory("2.3 SORT SAN PHAM");
            test.info("<b>[BAI 2.3 - GIO HANG]</b> Kiem tra sap xep san pham");
        } else if (methodName.contains("checkout") || methodName.contains("trong")) {
            test.assignCategory("2.3 CHECKOUT FORM");
            test.info("<b>[BAI 2.3 - THANH TOAN]</b> Kiem tra form checkout");
        } else if (methodName.contains("tongtien") || methodName.contains("gia") || methodName.contains("shipping")) {
            test.assignCategory("2.3 TINH TOAN TIEN");
            test.info("<b>[BAI 2.3 - THANH TOAN]</b> Kiem tra tinh toan tong tien");
        } else if (methodName.contains("cancel") || methodName.contains("continue")) {
            test.assignCategory("2.3 DIEU HUONG");
            test.info("<b>[BAI 2.3 - DIEU HUONG]</b> Kiem tra cancel / quay lai");
        } else if (methodName.contains("hoanthanh") || methodName.contains("reset") || methodName.contains("complete")) {
            test.assignCategory("2.3 HOAN THANH DON");
            test.info("<b>[BAI 2.3 - THANH TOAN]</b> Kiem tra hoan thanh don hang");
        } else if (methodName.contains("problem")) {
            test.assignCategory("2.3 PROBLEM USER");
            test.info("<b>[BAI 2.3 - BUG]</b> Kiem tra voi problem_user");
        } else {
            test.assignCategory("2.3 GIO HANG & THANH TOAN");
            test.info("<b>[BAI 2.3]</b> Kiem tra gio hang va thanh toan");
        }

        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passCount++;
        ExtentTest test = testThread.get();
        if (test != null) {
            test.log(Status.PASS, "<span style='color:green;font-weight:bold'>PASSED</span>");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failCount++;
        ExtentTest test = testThread.get();
        if (test != null) {
            test.log(Status.FAIL, "<span style='color:red;font-weight:bold'>FAILED:</span> " + result.getThrowable().getMessage());

            try {
                WebDriver driver = getDriverFromResult(result);
                if (driver != null && driver instanceof TakesScreenshot) {
                    String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    test.fail("Screenshot khi FAIL:",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
                }
            } catch (Exception e) {
                test.warning("Khong the chup screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipCount++;
        ExtentTest test = testThread.get();
        if (test != null) {
            test.log(Status.SKIP, "<span style='color:orange;font-weight:bold'>SKIPPED</span>");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        finishCount++;
        // Chi flush report khi tat ca cac <test> da chay xong
        if (extent != null && finishCount >= totalContexts) {
            int total = passCount + failCount + skipCount;
            String passRate = total > 0 ? String.format("%.1f%%", passCount * 100.0 / total) : "0%";
            String failRate = total > 0 ? String.format("%.1f%%", failCount * 100.0 / total) : "0%";
            String skipRate = total > 0 ? String.format("%.1f%%", skipCount * 100.0 / total) : "0%";

            String summaryHtml =
                "<div class='summary-container'>" +
                "  <div class='summary-title'>TONG HOP KET QUA KIEM THU</div>" +
                "  <div class='summary-box summary-total'>TONG: " + total + "</div>" +
                "  <div class='summary-box summary-pass'>PASS: " + passCount + "</div>" +
                "  <div class='summary-box summary-fail'>FAIL: " + failCount + "</div>" +
                "  <div class='summary-box summary-skip'>SKIP: " + skipCount + "</div>" +
                "  <br/><br/>" +
                "  <table style='margin:auto;border-collapse:collapse;min-width:500px;box-shadow:0 2px 8px rgba(0,0,0,0.1);border-radius:8px;overflow:hidden;'>" +
                "    <tr style='background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:white;'>" +
                "      <th style='padding:12px 25px;font-size:16px;'>Trang thai</th>" +
                "      <th style='padding:12px 25px;font-size:16px;'>So luong</th>" +
                "      <th style='padding:12px 25px;font-size:16px;'>Ti le</th>" +
                "    </tr>" +
                "    <tr style='background:#d5f5e3;'>" +
                "      <td style='padding:10px 25px;font-weight:bold;color:#27ae60;font-size:15px;'>PASS</td>" +
                "      <td style='padding:10px 25px;text-align:center;font-size:20px;font-weight:bold;'>" + passCount + "</td>" +
                "      <td style='padding:10px 25px;text-align:center;font-size:15px;'>" + passRate + "</td>" +
                "    </tr>" +
                "    <tr style='background:#fadbd8;'>" +
                "      <td style='padding:10px 25px;font-weight:bold;color:#e74c3c;font-size:15px;'>FAIL</td>" +
                "      <td style='padding:10px 25px;text-align:center;font-size:20px;font-weight:bold;'>" + failCount + "</td>" +
                "      <td style='padding:10px 25px;text-align:center;font-size:15px;'>" + failRate + "</td>" +
                "    </tr>" +
                "    <tr style='background:#fdebd0;'>" +
                "      <td style='padding:10px 25px;font-weight:bold;color:#f39c12;font-size:15px;'>SKIP</td>" +
                "      <td style='padding:10px 25px;text-align:center;font-size:20px;font-weight:bold;'>" + skipCount + "</td>" +
                "      <td style='padding:10px 25px;text-align:center;font-size:15px;'>" + skipRate + "</td>" +
                "    </tr>" +
                "    <tr style='background:#eaf2f8;border-top:2px solid #2c3e50;'>" +
                "      <td style='padding:10px 25px;font-weight:bold;color:#2c3e50;font-size:15px;'>TONG CONG</td>" +
                "      <td style='padding:10px 25px;text-align:center;font-size:20px;font-weight:bold;color:#2c3e50;'>" + total + "</td>" +
                "      <td style='padding:10px 25px;text-align:center;font-size:15px;font-weight:bold;'>100%</td>" +
                "    </tr>" +
                "  </table>" +
                "</div>";

            ExtentTest summary = extent.createTest("TONG HOP KET QUA")
                    .assignCategory("BANG TONG HOP");
            summary.info(summaryHtml);

            if (failCount == 0) {
                summary.pass("Tat ca " + passCount + " test case deu PASS!");
            } else {
                summary.fail("Co " + failCount + "/" + total + " test case FAIL!");
            }

            extent.flush();
        }
    }

    private WebDriver getDriverFromResult(ITestResult result) {
        try {
            Object testInstance = result.getInstance();
            if (testInstance instanceof base.BaseTest) {
                return ((base.BaseTest) testInstance).getDriver();
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    private String escapeHtml(String text) {
        if (text == null) return "null";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
