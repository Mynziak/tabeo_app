package core;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.AssertionException;

import java.util.ArrayList;
import java.util.List;

import static core.DriverHelp.getDriverWait;

//** Class represents methods that allow searching and interacting with WebDriverElement.
public class Elem {
    private By by;
    private boolean assertIt = false;
    private List<Class<? extends Throwable>> ignoredExceptions;
    private long timeout = 10;
    private String name;

    public Elem(By by, String name) {
        this.by = by;
        this.name = name;
        this.ignoredExceptions = new ArrayList<>();
    }

    public Elem(By by) {
        this.by = by;
        this.name = by.toString();
        this.ignoredExceptions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public By getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "'" + name + "'" + " (" + by + ")";
    }

    /**
     * Set Timeout for all actions with this Element
     *
     * @param timeout
     * @return
     */
    public Elem timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Next Action will be Asserted
     *
     * @return
     */
    public Elem assertion() {
        assertIt = true;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public List<WebElement> finds(long timeout) {
        return getDriverWait(timeout)
                .withMessage("Element: " + name + " not found on page: ")
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public void type(String text, long timeout) {
        Allure.step("Type: " + text + " in " + toString(), () -> {
                    find(timeout).clear();
                    find(timeout).sendKeys(new CharSequence[]{text});
                }
        );
    }

    public void type(String text) {
        type(text, timeout);
    }

    public void click() {
        click(timeout);
    }

    public void click(long timeout) {
        Allure.step("Click " + toString(), () -> {
            find(timeout);
            getDriverWait(timeout).until(ExpectedConditions.elementToBeClickable(by));
            getDriverWait(timeout).until(click(by));
        });
    }

    private static ExpectedCondition<Boolean> click(final By locator) {
        return new ExpectedCondition<Boolean>() {
            String message = "";

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    driver.findElement(locator).click();
                    return true;
                } catch (NoSuchElementException | StaleElementReferenceException | ElementClickInterceptedException e) {
                    message = e.getMessage();
                    return false;
                }
            }

            @Override
            public String toString() {
                return "Click Element: " + message;
            }
        };
    }

    public WebElement find() {
        return find(timeout);
    }

    public WebElement find(long timeout) {
        return getDriverWait(timeout).withMessage(toString() + " not found on page").until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public String getInnerText() {
        return find().getAttribute("innerHTML");
    }

    public String getAttribute(String name) {
        return getAttribute(name, timeout);
    }

    public String getAttribute(String name, long timeout) {
        String value = find(timeout).getAttribute(name);
        Allure.step("Value [" + name + "] attribute of Element: " + toString() + "= " + value);
        return value;
    }

    private void checkAssert(Object detailMessage) {
        if (assertIt) {
            assertIt = false;
            throw new AssertionException(this.by.toString() + "\n" + detailMessage);
        }
    }

    public boolean isPresent(long timeout) {
        try {
            getDriverWait(timeout).ignoreAll(this.ignoredExceptions).until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            this.checkAssert(this.toString() + " is Absent after: " + timeout + " sec \n" + e);
            return false;
        } finally {
            this.assertIt = false;
            this.ignoredExceptions.clear();
        }
    }

    public boolean isPresent() {
        return isPresent(timeout);
    }

    public boolean isNotPresent(long timeout) {
        try {
            getDriverWait(timeout).until(ExpectedConditions.numberOfElementsToBe(by, 0));
            return true;
        } catch (TimeoutException e) {
            checkAssert("Element still present! \n" + e);
            return false;
        } finally {
            assertIt = false;
        }
    }

    public boolean isNotPresent() {
        return isNotPresent(timeout);
    }

    public boolean isNotVisible(long timeout) {
        try {
            getDriverWait(timeout).until(ExpectedConditions.invisibilityOf(find(timeout)));
            return true;
        } catch (AssertionError | TimeoutException e) {
            checkAssert(e);
            return false;
        } finally {
            assertIt = false;
        }
    }

    public boolean isVisible(long timeout) {
        try {
            getDriverWait(timeout).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
            Allure.step("Elem is visible= " + this.toString());
            return true;
        } catch (StaleElementReferenceException | TimeoutException e) {
            checkAssert(e);
            return false;
        } finally {
            assertIt = false;
        }
    }

    public boolean isVisible() {
        return isVisible(timeout);
    }

    public boolean isNotVisible() {
        return isNotVisible(timeout);
    }

    public boolean isEnabled(long timeout) {
        try {
            getDriverWait(timeout).until(ExpectedConditions.elementToBeClickable(find(timeout)));
            return true;
        } catch (StaleElementReferenceException | TimeoutException e) {
            checkAssert(e);
            return false;
        } finally {
            assertIt = false;
        }
    }

    @Step("Hover over and click")
    public void hoverOverAndClick() {
        Actions action = new Actions(DriverHelp.getDriver());
        WebElement webElement = this.find();
        action.moveToElement(webElement).click().build().perform();
    }
}
