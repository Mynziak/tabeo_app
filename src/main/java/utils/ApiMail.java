package utils;

import io.qameta.allure.Step;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.testng.Assert.assertTrue;

public class ApiMail {

    public static final String HOST_GMAIL = "pop.gmail.com";

    //Method must be synchronized to prevent simultaneously connection to the same email account
    public static synchronized Store connectToEmail(String email, String pwd) {
        Store storeObj = null;
        try {
            //Set property values
            Properties propvals = new Properties();
            propvals.put("mail.pop3.host", HOST_GMAIL);
            propvals.put("mail.pop3.port", "995");
            propvals.put("mail.pop3.starttls.enable", "true");
            Session emailSessionObj = Session.getDefaultInstance(propvals);
            storeObj = emailSessionObj.getStore("pop3s");
            storeObj.connect(HOST_GMAIL, email, pwd);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return storeObj;
    }

    public static int getAllMessagesNumber(String email, String pwd) {
        Store storeObj = connectToEmail(email, pwd);

        int mailsNumber = 0;
        try {
            //Create folder object and open it in read-only mode and filter by the subject
            Folder emailFolderObj = storeObj.getFolder("INBOX");
            emailFolderObj.open(Folder.READ_ONLY);

            mailsNumber = emailFolderObj.getMessages().length;

            //Now close all the objects
            emailFolderObj.close(false);
            storeObj.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return mailsNumber;
    }

    @Step("Get last message with subject [{2}]")
    public static String getLastMailWithSubject(String email, String pwd, String sbj) {
        Store storeObj = connectToEmail(email, pwd);

        String lastMailContent = "";
        try {
            //Create folder object and open it in read-only mode and filter by the subject
            Folder emailFolderObj = storeObj.getFolder("INBOX");
            emailFolderObj.open(Folder.READ_ONLY);

            List<Message> tabeoMessages = Arrays.asList(emailFolderObj.getMessages());
            Optional<Message> last = tabeoMessages.stream()
                    .filter(m -> {
                        try {
                            return m.getSubject().contains(sbj);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }).reduce((first, second) -> second);
            assertTrue(last.isPresent(), "Emails with subject [" + sbj + "] are absent!");
            lastMailContent = getTextFromMessage(last.get());

            //Now close all the objects
            emailFolderObj.close(false);
            storeObj.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        return lastMailContent;
    }

    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice sometimes
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }

        return result;
    }

    public static void deleteAllMails(String email, String pwd) {
        try {
            Store storeObj = connectToEmail(email, pwd);

            //Create folder object and open it in READ_WRITE mode
            Folder emailFolderObj = storeObj.getFolder("INBOX");

            emailFolderObj.open(Folder.READ_WRITE);

            Arrays.asList(emailFolderObj.getMessages()).forEach(m -> {
                try {
                    m.setFlag(Flags.Flag.DELETED, true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });

            //Now close all the objects
            emailFolderObj.close(true);
            storeObj.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

}
