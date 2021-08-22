package pages;

import core.Elem;
import org.openqa.selenium.By;

public class GoogleLoginPage {

    public static final String PAGE_NAME = "Google Login";

    public void signInGoogle(String email, String pwd) {
        Elem emailInput = new Elem(By.cssSelector("#identifierId"), "emailInput");
        emailInput.assertion().isPresent(15);
        emailInput.type(email);

        Elem nextBtn = new Elem(By.cssSelector("#identifierNext"));
        nextBtn.assertion().isPresent();
        nextBtn.click();

        Elem passwordInput = new Elem(By.cssSelector("[name='password']"));
        passwordInput.assertion().isEnabled(10);
        passwordInput.type(pwd);

        Elem passwordNextButton = new Elem(By.cssSelector("#passwordNext"));
        passwordNextButton.assertion().isPresent();
        passwordNextButton.click();
        passwordNextButton.assertion().isNotPresent();
    }

}
