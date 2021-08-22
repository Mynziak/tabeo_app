package pages;

import core.Elem;
import io.qameta.allure.Step;
import listeners.TestListener;
import org.openqa.selenium.By;

public class SignInWindow {

    public Elem signInWithEmailBtn = new Elem(By.xpath("//button[@type='submit']"), "Sign In with Email button"); //TODO try id=submit

    @Step("Check 'Sign in to your account' is opened")
    public void checkIsOpened() {
        signInWithEmailBtn.assertion().isPresent(15);
    }

    @Step("Sign In with email [{0}]")
    public void signInWithEmail(String email) {
        checkIsOpened();

        Elem emailInput = new Elem(By.id("email"), "Email input field");
        emailInput.assertion().isPresent();
        emailInput.type(email);

        signInWithEmailBtn.assertion().isPresent();
        signInWithEmailBtn.click();

        //Check success message:
        Elem successSendingMessage = new Elem(By.xpath("//p[contains(.,'A sign in link has been sent to your email address.')]"), "A sign in link has been sent");
        successSendingMessage.assertion().isPresent();
    }

    @Step("Close Sign In window")
    public void close(){
        Elem hiddernMainPage = new Elem(By.id("headlessui-portal-root"));
        hiddernMainPage.assertion().isPresent();
        hiddernMainPage.hoverOverAndClick();
        signInWithEmailBtn.assertion().isNotPresent();
    }

    @Step("Click on Sign In with Google")
    public void signWithGoogle() {
        Elem signWithGoogleBtn = new Elem(By.xpath("//button[contains(.,'Sign in with Google')]"), "Sign in with Google");
        signWithGoogleBtn.assertion().isPresent();
        TestListener.printLog();  //TODO: need to investigate. without printing log - sometimes tests are hanging
        signWithGoogleBtn.click();
        signWithGoogleBtn.assertion().isNotPresent();
//        TestListener.printLog();
    }

}
