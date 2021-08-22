package utils;

import org.awaitility.core.ConditionTimeoutException;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class Wait {

    public static void waitItem(Callable<Boolean> conditionEvaluator, String message, long timeout, Class<? extends Throwable> ignoredException) {
        try {
            await(message)
                    .pollInSameThread()
                    .timeout(timeout, TimeUnit.SECONDS)
                    .ignoreException(ignoredException)
                    .until(conditionEvaluator);
        } catch (ConditionTimeoutException e) {
            throw new AssertionError(message, e);
        }
    }

    public static void waitItem(Callable<Boolean> conditionEvaluator, String message, long timeout) {
        waitItem(conditionEvaluator, message, timeout, AssertionError.class);
    }

}
