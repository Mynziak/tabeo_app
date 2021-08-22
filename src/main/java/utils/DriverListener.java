package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DriverListener implements ISuiteListener {

    @Override
    public void onFinish(ISuite iSuite) {

    }

    /*protected void attachAppiumLog() {
        if (!AppiumServerMan.getAppiumLogFilePath().isEmpty()) {
            String appiumLogFileName = AppiumServerMan.getAppiumLogFilePath().replaceAll("^(.*[\\\\\\/])", "");
            AllureAttachments.htmlAttachment(appiumLogFileName, AppiumServerMan.getAppiumLogFilePath());
        } else {
            logger.warn("Appium Server Log is absent!");
        }
    }

    protected void takeScreenshot(ITestResult result) {
        String screenshotName = result.getName() + "_" + TimeMan.getCurrentTime() + ".png"; // info for name of screenshot
        AppiumDriver<MobileElement> driver = DriverMan.getDriver();
        if (driver != null) {
            File scrFile = null;
            try {
                scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            } catch (org.openqa.selenium.NoSuchSessionException e) {
                logger.warn("Impossible take screenshot, because driver session is either terminated or not started! Check Appium Server Log.");
            }
            String screenshotDir = getTestReportDir(AppiumServerMan.getAppiumLogFilePath()) + screenshotName;
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
    }*/
}
