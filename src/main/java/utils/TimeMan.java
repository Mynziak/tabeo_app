package utils;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeMan {
    private static Logger logger = LoggerFactory.getLogger(TimeMan.class);

    public static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd__HH_mm_ss"));
    }

    public static String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getTS() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        return String.valueOf(ts.getTime());
    }

    public static String format(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static String getCurrentDatePlusOneMonth(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }

    public static void sleep(int seconds) {
        logger.info("Sleeping for [" + seconds + "] seconds");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(double minutes) {
        logger.info("Sleeping for [" + minutes + "] minutes");
        try {
            Thread.sleep((long) (minutes * 60 * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Sleep for [{0}] milliseconds")
    public static void sleep(long millis) {
        logger.info("Sleeping for [" + millis + "] milliseconds");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long getDateDiff(Date first, Date second, TimeUnit timeUnit) {
        long diffInMillis = second.getTime() - first.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
