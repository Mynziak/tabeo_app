package core;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileMan;
import utils.TimeMan;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

//** Setup Web Driver configuration and initialize
public class DriverHelp {
    private static Logger logger = LoggerFactory.getLogger(DriverHelp.class);

    public static final String DRIVER_SOURCE = "src/main/resources/chromedriver";
    public static final String TEST_REPORTS_DIR_NAME = "test-reports";

    private static final ThreadLocal<WebDriver> DRIVER_TL = new ThreadLocal<>();

    private static final ThreadLocal<String> DOWNLOAD_DIR = new ThreadLocal<>();

    //STATIC ALL THREADS Drivers LIST TO CLOSE ON Shutdown
    public static List<WebDriver> staticDrivers = Collections.synchronizedList(new ArrayList<>());

    private static synchronized void addStaticDriver(WebDriver driver) {
        staticDrivers.add(driver);
    }

    public static WebDriver getDriver() {
        return DRIVER_TL.get();
    }

    public static WebDriver initDriver() {
        String downloadFilepath = setupDownloadDirPath(); //unique for each test
        DOWNLOAD_DIR.set(downloadFilepath);

        ChromeDriverService service;
        WebDriver driver;

        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(DRIVER_SOURCE))
                .usingAnyFreePort()
                .build();
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Setup browser configuration:
        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--disable-notifications");
        options.addArguments("--start-fullscreen"); //open browser on fullscreen
        options.addArguments("--test-type");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-plugins");

        //Setup Logging configuration
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logPrefs);

        driver = new RemoteWebDriver(service.getUrl(), options);

        DRIVER_TL.set(driver);
        addStaticDriver(driver);
        return driver;
    }

    @Step("Open new tab")
    public static void openNewTab() {
        JavascriptExecutor js = (JavascriptExecutor) DriverHelp.getDriver();
        js.executeScript("window.open();");
        Set<String> windows = getDriver().getWindowHandles();
        String openedWindow = windows.stream().collect(Collectors.toList()).get(windows.size() - 1);
        getDriver().switchTo().window(openedWindow);
    }

    public static String getDownloadDir() {
        return DOWNLOAD_DIR.get();
    }

    private static synchronized String setupDownloadDirPath() {
        String suiteName = org.testng.Reporter.getCurrentTestResult().getTestContext().getName(); //get Suite name to setup download dir for each test
        String downloadDir = System.getProperty("user.dir") + "/" + "target" + "/" + TEST_REPORTS_DIR_NAME + "/" + suiteName + "/" + TimeMan.getCurrentDate() + "/" + TimeMan.getCurrentTime();

        FileMan.createDirectory(downloadDir);
        return downloadDir;
    }

    public static WebDriverWait getDriverWait(long timeOutInSeconds) {
        return new WebDriverWait(getDriver(), timeOutInSeconds);
    }

    @Step("Driver quit")
    public static void driverQuit() {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }

    @Step("Close All browsers")
    public static void closeAllBrowsers() {
        if (!staticDrivers.isEmpty()) {
            staticDrivers.forEach(d -> d.quit());
            staticDrivers.clear();
        }
    }

    @Step("Open URL [{0}]")
    public static void openUrl(String url) {
        getDriver().get(url);
        waitPageLoadComplete();
    }

    private static void waitPageLoadComplete() {
        WebDriverWait wait = getDriverWait(15);
        wait.until(webDriver -> ((JavascriptExecutor) getDriver())
                .executeScript("return document.readyState")
                .toString()
                .equals("complete"));
    }

    @Step("Press browser back button")
    public static void navigateBack() {
        DriverHelp.getDriver().navigate().back();
        waitPageLoadComplete();
    }

    @Step("Refresh browser page")
    public static void refresh() {
        DriverHelp.getDriver().navigate().refresh();
        waitPageLoadComplete();
    }
}
