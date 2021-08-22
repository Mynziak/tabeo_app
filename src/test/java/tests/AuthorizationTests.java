package tests;

import core.DriverHelp;
import data.TestData;
import org.testng.annotations.Test;
import utils.Credentials;
import utils.MailerMan;

import static core.DriverHelp.openNewTab;
import static core.DriverHelp.openUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AuthorizationTests extends TabeoBasicTest {

    private String googleURL = "http://accounts.google.com/signin";

    @Test(description = "Sign in by Google Email", priority = -1)
    //without priority sometimes tests are hanging on opening Google page
    public void signInByGoogleTest() {
        openMainPage();
        pages.main.signIn();
        pages.signInWindow.signWithGoogle();
        pages.googleLoginPage.signInGoogle(Credentials.EMAIL, Credentials.PASSWORD);
        assertTrue(pages.main.isAuthorized(), "User is not authorized by Google!");
    }

    @Test(description = "Sign in by Google account", priority = 1)
    public void authByGoogleTest() {
        DriverHelp.initDriver();
        openUrl(googleURL);
        pages.googleLoginPage.signInGoogle(Credentials.EMAIL, Credentials.PASSWORD);

        //Open Tabeo url in new tab:
        openNewTab();
        openUrl(TestData.TABEO_URL);
        pages.main.signIn();
        pages.signInWindow.signWithGoogle();

        assertTrue(pages.main.isAuthorized(), "User is not authorized by Google!");
    }

    @Test(description = "Sign In by Email Test", priority = 0)
    public void signInByEmailTest() {

        String email = Credentials.generateEmail();
        openMainPage();

        pages.main.signIn();

        pages.signInWindow.signInWithEmail(email);

        //Get auth link from email:
        String authLink = MailerMan.getAuthLink(email);

        //Sign In and check account email:
        openUrl(authLink);
        pages.main.checkIsAuthorized();

        String account = pages.main.getAccountEmail();
        assertEquals(account, email, "Account email is wrong!");
    }

    @Test(description = "Sign Out Test")
    public void signOutTest() {
        String email = Credentials.generateEmail();

        openMainPage();

        pages.main.signIn();
        pages.signInWindow.signInWithEmail(email);

        //Get auth link from email:
        String authLink = MailerMan.getAuthLink(email);

        //Sign In Tabeo:
        openUrl(authLink);
        pages.main.checkIsAuthorized();

        pages.main.signOut();

        assertTrue(pages.main.signInBtn.isPresent(10), "User is not Signed Out!");
    }

    @Test(description = "Check Sign In window by clicking Subscription Test")
    public void signInSubscriptionTest() {
        openMainPage();
        pages.main.clickSubscription();
        pages.signInWindow.checkIsOpened();
    }

    @Test(description = "Check Sign In window by clicking Single Payment Test")
    public void signInSinglePaymentTest() {
        openMainPage();
        pages.main.clickSinglePay();
        pages.signInWindow.checkIsOpened();
    }
}
