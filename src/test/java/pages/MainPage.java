package pages;

import core.Elem;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static utils.Check.checkTrue;

public class MainPage {

    public Elem subscribtionBtn = new Elem(By.xpath("//button[contains(.,'Pay £20/mo')]"), "Pay 20£/mo");
    private Elem accountBtn = new Elem(By.id("headlessui-menu-button-7"), "Account button");
    public Elem signInBtn = new Elem(By.xpath("//button[contains(.,'Sign in')]"), "Sign In button");

    @Step("Click on Sgin in")
    public void signIn() {
        signInBtn.assertion().isPresent();
        signInBtn.click();
    }

    @Step("Click on subscription 'Pay £20/mo' button")
    public void clickSubscription() {
        subscribtionBtn.assertion().isPresent();
        subscribtionBtn.click();
    }

    @Step("Click on subscription 'Pay £220' button")
    public void clickSinglePay() {
        Elem singlePayBtn = new Elem(By.xpath("//button[contains(.,'Pay £220')]"), "Pay £220");
        singlePayBtn.assertion().isPresent();
        singlePayBtn.click();
    }

    @Step("Close 'Success sent mail' message")
    public void clickOverTheMessage() {
        Elem mainPage = new Elem(By.id("headlessui-portal-root"));
        mainPage.assertion().isPresent();
        mainPage.hoverOverAndClick();
    }

    @Step("Check is User authorized")
    public void checkIsAuthorized() {
        checkTrue(isAuthorized(), "User is not athorized");
    }

    @Step("Is User authorized")
    public boolean isAuthorized() {
        return accountBtn.isPresent(20);
    }

    @Step("Get Account Email")
    public String getAccountEmail() {
        checkIsAuthorized();
        Elem accountEmail = new Elem(By.xpath("//*[@class='text-gray-600 text-sm mr-2']"), "Account email");

        accountEmail.assertion().isPresent(20);
        String email = accountEmail.getInnerText();
        return email;
    }

    @Step("Check that error is not occured")
    public void checkAppError() {
        Elem appError = new Elem(By.xpath("//*[contains(.,'Application error')]"));
        Assert.assertFalse(appError.isPresent(), "Application error occured");
    }

    @Step("Sign Out")
    public void signOut() {
        accountBtn.assertion().isPresent();
        accountBtn.click();

        Elem signOutBtn = new Elem(By.xpath("//div[contains(@id,'headlessui-menu-items')]/button"), "Sign Out button");
        signOutBtn.assertion().isPresent();
        signOutBtn.click();
        signOutBtn.assertion().isNotPresent();
    }

}
