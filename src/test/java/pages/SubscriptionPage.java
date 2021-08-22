package pages;

import core.Elem;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.List;

public class SubscriptionPage extends PaymentBase implements PaymentI {

    @Override
    public PaymentI checkPrice() {
        Elem price = new Elem(By.xpath("//*[@class='mr2 flex-item width-fixed']"));
        price.assertion().isPresent(15);
        price.assertion().isVisible();
        String priceValue = price.getInnerText();
        Assert.assertEquals(priceValue, "£20.00", "Price Value is incorrect!");
        return this;
    }

    @Override
    public void payWithCard(List<String> c) {
        fillCard(c);
        subscribe();
    }

    @Step("Click on Subscribe")
    public void subscribe(){
        Elem subscribeBtn = new Elem(By.cssSelector(".SubmitButton-IconContainer"));
        subscribeBtn.assertion().isPresent();
        subscribeBtn.click();
    }
}
