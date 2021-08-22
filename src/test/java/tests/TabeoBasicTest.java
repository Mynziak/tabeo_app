package tests;

import core.DriverHelp;
import io.qameta.allure.Step;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import pages.Pages;
import utils.Credentials;
import utils.FileMan;
import utils.MailerMan;

import java.util.List;

import static core.DriverHelp.initDriver;
import static core.DriverHelp.openUrl;
import static data.TestData.*;
import static utils.Check.checkEquals;
import static utils.Credentials.EMAIL;

public class TabeoBasicTest {
    public Pages pages = new Pages();

    @Step("Open Tabeo App Main page")
    protected void openMainPage() {
        initDriver();
        openUrl(TABEO_URL);
        pages.main.subscribtionBtn.assertion().isPresent(30);
    }

    protected String getDownloadedZipPath() {
        return DriverHelp.getDownloadDir() + "/" + APP_ZIP_NAME;
    }

    protected String getDownloadedFilePath() {
        return DriverHelp.getDownloadDir() + "/" + APP_DIR_NAME + "/" + APPLICATION_NAME;
    }

    protected List<String> getDownloadedFiles() {
        String downloadedZipPath = getDownloadedZipPath();
        FileMan.waitForDownloading(downloadedZipPath);

        return FileMan.extractZipContent(downloadedZipPath);
    }

    protected void checkAuthorization(String email) {
        String account = pages.main.getAccountEmail();
        checkEquals(account, email, "Account email is wrong!");
    }

    @AfterMethod
    public void tearDownTest() {
        //close browser:
        DriverHelp.driverQuit();

        //clean Download folder
        String downloadDirPath = DriverHelp.getDownloadDir();
        String appFile = getDownloadedFilePath();
        FileMan.cleanDirectoryFiles(appFile);
        FileMan.cleanDirectory(downloadDirPath);
    }

    @AfterClass
    public void cleanEmails() {
        MailerMan.deleteAllMessages(EMAIL); //clean parent Email from emails
    }

    @AfterSuite
    public void tearDownSuite() {
        DriverHelp.closeAllBrowsers();
    }
}
