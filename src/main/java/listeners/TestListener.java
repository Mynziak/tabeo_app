package listeners;

import core.DriverHelp;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.AllureAttachments;
import utils.FileMan;
import utils.TimeMan;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static core.DriverHelp.TEST_REPORTS_DIR_NAME;

public class TestListener extends TestListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onFinish(ITestContext Result) {
        printLog();
    }

    @Override
    public void onStart(ITestContext Result) {
        logger.error("Test: " + Result.getName() + "  is started...");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult Result) {

    }

    @Override
    public void onTestFailure(ITestResult Result) {
        logger.error("Test: " + Result.getName() + "  is failed!");
        takeScreenshot(Result);
    }

    @Override
    public void onTestSkipped(ITestResult Result) {
        logger.info("Test: " + Result.getName() + "  is skipped!");
    }

    @Override
    public void onTestSuccess(ITestResult Result) {
        logger.info("Test: " + Result.getName() + " has successfully finished.");
    }

    public static void printLog() {
        printLog(LogType.BROWSER);
        printLog(LogType.DRIVER);
    }

    public static void printLog(String logType) {
        try {
            WebDriver driver = DriverHelp.getDriver();
            if (driver != null) {
                Logs logs = driver.manage().logs();
                LogEntries logEntries = logs.get(logType);

                for (LogEntry logEntry : logEntries) {
                    logger.info(logEntry.getMessage());
                }
            }
        } catch (Exception e) {
            logger.warn("Impossible to capture the log, because driver is absent!");
        }
    }

    protected void takeScreenshot(ITestResult result) {
        String screenshotName = result.getName() + "_" + TimeMan.getCurrentTime() + ".png"; // info for name of screenshot
        WebDriver driver = DriverHelp.getDriver();
        if (driver != null) {
            File scrFile = null;
            try {
                scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            } catch (org.openqa.selenium.NoSuchSessionException e) {
                logger.warn("Impossible take screenshot, because driver session is either terminated or not started!");
            }
            String suiteName = org.testng.Reporter.getCurrentTestResult().getTestContext().getName(); //get Suite name to setup download dir for each test
            String scrDir = System.getProperty("user.dir") + "/" + "target" + "/" + TEST_REPORTS_DIR_NAME + "/" + suiteName + "/" + TimeMan.getCurrentDate() + "/";

            String screenshotDir = scrDir + screenshotName;
            FileMan.createFile(screenshotDir);
            if (scrFile != null) {
                try {
                    ImageIO.read(scrFile);
                    FileUtils.copyFile(scrFile, new File(screenshotDir));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AllureAttachments.imageAttachment(screenshotName, screenshotDir);
            } else {
                logger.warn("!! ---Screenshot is not taken because because driver session is either terminated or not started! Check Appium Server Log.");
            }
        } else {
            logger.warn("! ---Screenshot is not taken because because driver session is either terminated or not started! Check Appium Server Log.");
        }
    }

}
