package utils;

import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AllureAttachments {
    private static Logger logger = LoggerFactory.getLogger(AllureAttachments.class);

    @Attachment(value = "{0}", type = "text/html")
    public static byte[] htmlAttachment(String name, String htmlFilePath) {
        return getBytesFromFile(htmlFilePath);
    }

    @Attachment(value = "Video report", type = "text/html")
    public static byte[] selenoidHtmlAttachment(String stringHtml) {
        return stringHtml.getBytes();
    }

    @Attachment(value = "{0}", type = "video/avi")
    public static byte[] videoAttachment(String name, String videoFilePath) {
        return getBytesFromFile(videoFilePath);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String textAttachment(String name, String text) {
        return text;
    }

    @Attachment(value = "{0}", type = "image/png")
    public static byte[] imageAttachment(String name, String imageFilePath) {
        logger.info("Image attached from = " + imageFilePath);
        return getBytesFromFile(imageFilePath);
    }

    @Attachment(value = "{0}", type = "gifImage/gif")
    public static byte[] gifAttachment(String name, String gifFilePath) {
        return getBytesFromFile(gifFilePath);
    }

    @Attachment(value = "{1}", type = "application/json")
    public static String jsonAttachment(String json, String name) {
        return json;
    }

    private static byte[] getBytesFromFile(String filePath) {
        byte[] fileAsByteArray = "".getBytes();
        try {
            fileAsByteArray = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("File is not found! check filePath = " + filePath + "\n" + e.getMessage());
        }
        return fileAsByteArray;
    }
}
