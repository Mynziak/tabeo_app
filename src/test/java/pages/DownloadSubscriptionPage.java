package pages;

import data.TestData;
import io.qameta.allure.Step;
import utils.Check;

public class DownloadSubscriptionPage extends DownloadBase {

    @Step("Check Price after payment")
    public DownloadSubscriptionPage checkPrice() {
        waitForLoading();
        String actualPrice = getPrice();
        Check.checkEquals(actualPrice, TestData.SUBSCRIPTION_PRICE, "Price is incorrect!");
        return this;
    }

}
