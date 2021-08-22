package pages;

import core.DriverHelp;
import core.Elem;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.AssertionException;
import utils.Wait;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PaymentBase {

    protected void fillCard(List<String> c) {
        List<Elem> cardElems = new ArrayList<>();
        cardElems.add(new Elem(By.id("cardNumber"), "Card number input"));
        cardElems.add(new Elem(By.id("cardExpiry"), "Card expiry input"));
        cardElems.add(new Elem(By.id("cardCvc"), "Card CVV input"));
        cardElems.add(new Elem(By.id("billingName"), "Card Name input"));

        for (int i = 0; i < c.size(); i++) {
            Elem cardInput = cardElems.get(i);
            cardInput.assertion().isPresent();
            String input = c.get(i);
            cardInput.type(input);
        }
    }

    @Step("Click Back button")
    public void back(){
        Elem popcorn = new Elem(By.cssSelector(".Header-businessLink-label"), "Popcorn back button");
        popcorn.assertion().isPresent();
        popcorn.hoverOverAndClick();
        popcorn.assertion().isNotPresent();
    }

    @Step("Select is 'fail authentication' card [{0}] ")
    public void failAuth(boolean isFailAuth) {
        waitForPopupLoading();

        WebDriver driver = DriverHelp.getDriver();
        //Switch to appropriate iframes:
        Elem iframe_1 = new Elem(By.xpath("//iframe[contains(@name,'privateStripeFrame')]"), "First iframe");
        iframe_1.assertion().isPresent();
        String iframeName = iframe_1.getAttribute("name");
        driver.switchTo().frame(iframeName);

        Elem iframe_2 = new Elem(By.id("challengeFrame"), "Second iframe");
        iframe_2.assertion().isPresent();
        driver.switchTo().frame("challengeFrame");

        Elem iframe_3 = new Elem(By.xpath("//iframe[@name='acsFrame']"), "Third iframe");
        iframe_3.assertion().isPresent();
        driver.switchTo().frame("acsFrame");

        Elem failBtn;
        if (isFailAuth) {
            failBtn = new Elem(By.id("test-source-fail-3ds"), "FAIL ATH");
        } else {
            failBtn = new Elem(By.id("test-source-authorize-3ds"), "COMPLETE AUTH");
        }
        failBtn.assertion().isPresent();
        failBtn.click();

        driver.switchTo().defaultContent();
    }

    @Step("Get failed transaction error ")
    public String getFailedTransactionError(boolean isFailAuth) {
        String error;
        Elem paymentError;
        if (isFailAuth) {
            paymentError = new Elem(By.cssSelector(".ConfirmPayment-Error"));
            paymentError.assertion().isPresent();
            error = paymentError.getInnerText();
        } else {
            paymentError = new Elem(By.xpath("//span[contains(.,'Your card has been declined.')]"));
            paymentError.assertion().isPresent();
            String errorSpan = paymentError.getInnerText();
            error = extractError(errorSpan);
        }
        return error;
    }

    //Wait for testing failed transaction popup
    private void waitForPopupLoading() {
        Elem iframe = new Elem(By.xpath("//iframe[contains(@name,'privateStripeFrame')]"), "Base popup iframe");
        Wait.waitItem(() -> iframe.finds(5).size() > 1, "Test fail popup is absent!", 10);
    }

    private String extractError(String text) {
        String error ;
        Pattern p = Pattern.compile("\\>(.*?)\\<");
        Matcher m = p.matcher(text);
        if (m.find()) {
            error = m.group(1);
        } else {
            throw new AssertionException("Impossible extract error from dom content!");
        }
        return error;
    }
}
