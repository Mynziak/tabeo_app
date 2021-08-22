package tests;

import core.DriverHelp;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.MailerMan;

import static core.DriverHelp.openUrl;
import static utils.Check.checkTrue;
import static utils.Credentials.generateEmail;
import static utils.Credentials.succeedTransactionCard;

public class NavigationTests extends TabeoBasicTest {

    private String email;

    @BeforeMethod
    public void prepareTestData() {
        email = generateEmail();

        MailerMan.deleteAllMessages(email);  //Delete all messages from Email for getting new one

        openMainPage();
    }

    @Test(description = "Close Sign In window Test")
    public void closeSignInTest() {
        pages.main.signIn();
        pages.signInWindow.checkIsOpened();

        Assert.assertTrue(pages.main.signInBtn.isPresent(), "Sign In window is not closed!");
    }

    @Test(description = "Navigate back by breadcrumbs buttons Test")
    public void backFromPaymentBreadcrumbsTest() {

        //Sign In with email:
        pages.main.clickSubscription();
        pages.signInWindow.signInWithEmail(email);
        String authLink = MailerMan.getAuthLink(email);
        openUrl(authLink);
        checkAuthorization(email);

        //Pay
        pages.main.clickSubscription();
        pages.subscriptionPayment.checkPrice();
        pages.subscriptionPayment.back();
        Assert.assertTrue(pages.main.subscribtionBtn.isPresent(), "Back breadcrumb button doesn't work!");
    }

    @Test(description = "Navigate back with browser Test")
    public void backFromPaymentTest() {

        //Sign In with email:
        pages.main.clickSinglePay();
        pages.signInWindow.signInWithEmail(email);
        String authLink = MailerMan.getAuthLink(email);
        openUrl(authLink);
        checkAuthorization(email);

        //Pay
        pages.main.clickSinglePay();
        pages.singlePayment.checkPrice();

        DriverHelp.navigateBack(); //use browser back button

        Assert.assertTrue(pages.main.subscribtionBtn.isPresent(20), "Back breadcrumb button doesn't work!");
    }

    @Test(description = "Refresh pages Test")
    public void refreshPagesTest() {

        DriverHelp.refresh();
        checkTrue(pages.main.signInBtn.isPresent(), "Main page is not opened after refreshing!");

        pages.main.clickSubscription();
        pages.signInWindow.checkIsOpened();
        DriverHelp.refresh();
        checkTrue(pages.main.signInBtn.isPresent(), "Payment page is not closed after refreshing!");

        //sign In
        pages.main.clickSubscription();
        pages.signInWindow.checkIsOpened();
        pages.signInWindow.signInWithEmail(email);
        String authLink = MailerMan.getAuthLink(email);
        openUrl(authLink);
        checkAuthorization(email);

        DriverHelp.refresh();
        checkAuthorization(email);

        //Pay
        pages.main.clickSinglePay();
        pages.singlePayment.checkPrice();

        DriverHelp.refresh();
        pages.singlePayment
                .checkPrice()
                .payWithCard(succeedTransactionCard());


        //Check Download page after refresh
        pages.downloadSinglePayment.checkPrice();

        DriverHelp.refresh();
        pages.downloadSinglePayment.checkPrice();
    }

    @Test(description = "Try signIn after closing 'success message sent' Test")
    public void navigateToPaymentTest() {
        pages.main.clickSubscription();

        pages.signInWindow.signInWithEmail(email);
        pages.main.clickOverTheMessage();
        pages.main.clickSubscription();

        pages.main.checkAppError();
    }

}
