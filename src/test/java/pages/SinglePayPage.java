package pages;

import core.Elem;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.List;

public class SinglePayPage extends PaymentBase implements PaymentI {

    @Override
    public PaymentI checkPrice() {
        Elem price = new Elem(By.id("ProductSummary-totalAmount"));
        price.assertion().isPresent(15);
        price.assertion().isVisible();
        String priceValue = price.getInnerText();
        Assert.assertEquals(priceValue, "£220.00", "Price Value is incorrect!");
        return this;
    }

    @Override
    public void payWithCard(List<String> c) {
        fillCard(c);
        pay();
    }

    @Step("Click on Payment button")
    public void pay() {
        Elem payBtn = new Elem(By.cssSelector(".SubmitButton-IconContainer"));
        payBtn.assertion().isPresent();
        payBtn.click();
    }

}
