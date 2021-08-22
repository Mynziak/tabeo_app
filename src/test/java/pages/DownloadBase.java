package pages;

import core.Elem;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utils.Check;

import static utils.Check.checkEquals;

public abstract class DownloadBase {

    private Elem download = new Elem(By.xpath("//*[@href='/IconPack.zip']"), "Download button");

    @Step("Click Download button")
    public void download() {
        waitForLoading();
        download.click();
    }

    public void waitForLoading() {
        download.assertion().isPresent(15);
        checkEquals(download.getInnerText(), "Download", "Download button is absent!");
    }

    protected String getPrice() {
        Elem price = new Elem(By.xpath("(//dd)[3]"), "Price");
        price.assertion().isPresent();
        return price.getInnerText();
    }
}
