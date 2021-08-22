package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.FileMan;
import utils.MailerMan;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static core.DriverHelp.openUrl;
import static data.TestData.APPLICATION_NAME;
import static utils.Check.checkEquals;
import static utils.Credentials.*;

public class PaymentTests extends TabeoBasicTest {

    private String email;
    private String unableAuthError = "We are unable to authenticate your payment method. Please choose a different payment method and try again.";
    private String declinedError = "Your card has been declined.";

    @BeforeMethod
    public void prepareTestData() {
        email = generateEmail();

        MailerMan.deleteAllMessages(EMAIL);  //Delete all messages from Email for getting new one:

        openMainPage();
    }

    @Test(description = "Check failed transaction Test")
    public void failedTransactionTest() {
        SoftAssert softAssert = new SoftAssert();

        //Sign In by Subscription button
        pages.main.clickSubscription();
        pages.signInWindow.signInWithEmail(email);

        //Get auth link from email:
        String authLink = MailerMan.getAuthLink(email);

        //Sign In and check account email:
        openUrl(authLink);
        checkAuthorization(email);

        //Pay
        pages.main.clickSubscription();

        pages.subscriptionPayment
                .checkPrice()
                .payWithCard(failTransactionCard());

        //Fail authenticate your payment method by failing Auth:
        pages.subscriptionPayment.failAuth(true);
        String errorAuth = pages.subscriptionPayment.getFailedTransactionError(true);

        //Make declined transaction:
        pages.subscriptionPayment.subscribe();
        pages.subscriptionPayment.failAuth(false);
        String errorDeclined = pages.subscriptionPayment.getFailedTransactionError(false);

        softAssert.assertEquals(errorAuth, unableAuthError, "Incorrect error occurred an attempt to make failed transactions with Unable auth!");
        softAssert.assertEquals(errorDeclined, declinedError, "Incorrect error occurred an attempt to make declined transactions!");

        softAssert.assertAll();
    }

    @Test(description = "Download App by Subscription Test")
    public void downloadBySubscriptionTest()  {
        SoftAssert softAssert = new SoftAssert();

        //Sign In by Subscription button
        pages.main.clickSubscription();
        pages.signInWindow.signInWithEmail(email);

        String authLink = MailerMan.getAuthLink(email); //Get auth link from email:
        openUrl(authLink);
        checkAuthorization(email);

        //Subscribe
        pages.main.clickSubscription();

        pages.subscriptionPayment
                .checkPrice()
                .payWithCard(succeedTransactionCard());

        pages.downloadSubscription.
                checkPrice()
                .download();

        //Check Downloaded files:
        List<String> unzippedFiles = getDownloadedFiles();

        checkEquals(unzippedFiles.size(), 2, "Unzipped files are absent or wrong! ");
        String appFileName = unzippedFiles.get(0);
        String appFilePath = getDownloadedFilePath();

        softAssert.assertEquals(appFileName, APPLICATION_NAME, "Application name is incorrect!");
        softAssert.assertTrue(FileMan.fileExists(appFilePath), "Application file is absent or empty!");
        softAssert.assertAll();
    }

    @Test(description = "Download App by Single Payment Test")
    public void downloadBySinglePaymentTest() throws IOException, MessagingException {
        SoftAssert softAssert = new SoftAssert();

        //Sign In by single pay button
        pages.main.clickSinglePay();
        pages.signInWindow.signInWithEmail(email);

        //Get auth link from email:
        String authLink = MailerMan.getAuthLink(email);

        //Sign In and check account email:
        openUrl(authLink);
        checkAuthorization(email);

        //Pay
        pages.main.clickSinglePay();

        pages.singlePayment
                .checkPrice()
                .payWithCard(succeedTransactionCard());

        pages.downloadSinglePayment.
                checkPrice()
                .download();

        //Check Downloaded files:
        List<String> unzippedFiles = getDownloadedFiles();

        checkEquals(unzippedFiles.size(), 2, "Unzipped files are absent or wrong! ");
        String appFileName = unzippedFiles.get(0);
        String appFilePath = getDownloadedFilePath();

        softAssert.assertEquals(appFileName, APPLICATION_NAME, "Application name is incorrect!");
        softAssert.assertTrue(FileMan.fileExists(appFilePath), "Application file is absent or empty!");
        softAssert.assertAll();
    }
}
