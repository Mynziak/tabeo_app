package utils;

import io.qameta.allure.Step;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.awaitility.Awaitility.await;

public class MailerMan {
    private static Logger logger = LoggerFactory.getLogger(MailerMan.class);

    @Step("Get Auth link from Email Message")
    public static synchronized String getAuthLink(String email, String pwd)  {
        final String TABEO_MAIL_SBJ = "Sign in to qa-challenge-tabeo.vercel.app";

        //wait for email:
        waitForEmail(email, TABEO_MAIL_SBJ);

        String mailContent = ApiMail.getLastMailWithSubject(email, pwd, TABEO_MAIL_SBJ);
        String authLink = extractLink(mailContent);
        return authLink;
    }

    public static String getAuthLink(String email)  {
        return getAuthLink(email, Credentials.PASSWORD);
    }

    @Step("Waiting for email with subject = [{1}]")
    private static void waitForEmail(String email, String sbj) {

        //wait for receiving email:
        try {
            await().atMost(1, TimeUnit.MINUTES)
                    .pollInterval(1, TimeUnit.SECONDS)
                    .until(() -> ApiMail.getAllMessagesNumber(email, Credentials.PASSWORD), Matchers.equalTo(1));
        } catch (Exception e) {
            throw new AssertionException("Email is not received!");
        }
    }

    private static String extractLink(String contentMessage) {
        String link = "";
        Pattern p = Pattern.compile("(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})");
        Matcher m = p.matcher(contentMessage);
        if (m.find()) {
            link = m.group(1);
        } else {
            throw new AssertionException("Impossible extract auth link from message content!");
        }
        logger.info("Auth link =" + link);
        return link;
    }

    @Step("Delete emails for [{0}]")
    public static void deleteAllMessages(String email, String pwd) {
        ApiMail.deleteAllMails(email, pwd);
    }


    public static void deleteAllMessages(String email) {
//        deleteAllMessages(email, Credentials.PASSWORD);
        deleteAllMessages(Credentials.EMAIL, Credentials.PASSWORD);
    }
}
