package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Credentials {

    private static Logger logger = LoggerFactory.getLogger(Credentials.class);

    public static final String EMAIL = "quality.assurance.tabeo@gmail.com";
    public static final String PASSWORD = "Tabeo1234!";

    public static synchronized String generateEmail() {
        return generateEmail("");
    }

    public static synchronized String generateEmail(String part) {
        return generateEmail("quality.assurance.tabeo", part);
    }

    public static synchronized String generateEmail(String prefix, String part) {
        String email = prefix + "+" + part + (new Date()).getTime() + "t" + Thread.currentThread().getId() + "@gmail.com";
        logger.info("GENERATED EMAIL: " + email);
        return email;
    }

    public static final List<String> succeedTransactionCard() {
        List<String> card = new ArrayList<>();
        card.add("4242424242424242");
        card.add("09/n25");;
        card.add("111");
        card.add("Success Transaction");
        return card;
    }

    public static final List<String> failTransactionCard() {
        List<String> card = new ArrayList<>();
        card.add("4000008260003178");
        card.add("09/n25");;
        card.add("111");
        card.add("Fail Transaction");
        return card;
    }
}
