package pages;

import io.qameta.allure.Step;

import static data.TestData.SINGLE_PAYMENT_PRICE;
import static utils.Check.checkEquals;

public class DownloadSinglePayment extends DownloadBase {

    @Step("Check Price after Single payment")
    public DownloadSinglePayment checkPrice() {
        waitForLoading();
        String actualPrice = getPrice();
        checkEquals(actualPrice, SINGLE_PAYMENT_PRICE, "Price is incorrect!");
        return this;
    }
}
