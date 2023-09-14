package util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extentReports;

    @Deprecated
    public static ExtentReports getInstance() {
        if (extentReports == null) {
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extent-report.html");
            extentReports = new ExtentReports();
            extentReports.attachReporter(htmlReporter);

            // Optional: Set configurations for the HTML report
            htmlReporter.config().setDocumentTitle("Ahmer's Report");
            htmlReporter.config().setReportName("News Validation Report");
            htmlReporter.config().setTheme(Theme.STANDARD);
        }
        return extentReports;
    }

    public static ExtentTest createTest(String testName) {
        return getInstance().createTest(testName);
    }


    public static void logInfo(ExtentTest test, String message) {
        test.log(Status.INFO, message);
    }

    public static void logPass(ExtentTest test, String message) {
        test.log(Status.PASS, message);
    }

    public static void logFail(ExtentTest test, String message) {
        test.log(Status.FAIL, message);
    }

    public static void logError(ExtentTest test, String message) {
        test.log(Status.ERROR, message);
    }

    public static void logWarning(ExtentTest test, String message) {
        test.log(Status.WARNING, message);
    }

}

