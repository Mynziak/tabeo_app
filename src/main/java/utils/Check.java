package utils;

public class Check {

    public static void checkTrue(boolean condition, String message) {
        if (!condition) {
            String errorMessage = "Assert True: " + message + ", expected [true] but found [false]";
            throw new AssertionException(errorMessage);
        }
    }

    public static void checkFalse(boolean condition, String message) {
        if (condition) {
            String errorMessage = "Assert False: " + message + ", expected [false] but found [true]";
            throw new AssertionException(errorMessage);
        }
    }

    public static void checkEquals(boolean actual, boolean expected, String message) {
        if (actual != expected) {
            String errorMessage = "Assert Equals: " + message + ", expected [" + expected + "] but found [" + actual + "]";
            throw new AssertionException(errorMessage);
        }
    }

    public static void checkEquals(int actual, int expected, String message) {
        if (actual != expected) {
            String errorMessage = "Assert Equals: " + message + ", expected [" + expected + "] but found [" + actual + "]";
            throw new AssertionException(errorMessage);
        }
    }

    public static void checkEquals(String actual, String expected, String message) {
        if (!(actual == null && expected == null)) {
            if (!actual.equals(expected)) {
                String errorMessage = "Assert Equals: " + message + ", expected [" + expected + "] but found [" + actual + "]";
                throw new AssertionException(errorMessage);
            }
        }
    }

    public static void checkNotEquals(int actual, int expected, String message) {
        if (actual == expected) {
            String errorMessage = "Assert Not Equals: " + message + ", expected [" + expected + "] equals to [" + actual + "]";
            throw new AssertionException(errorMessage);
        }
    }

    public static void checkNotEquals(String actual, String expected, String message) {
        if (actual == null && expected == null) {
            String errorMessage = "Assert Not Equals: " + message + ", expected [" + expected + "] equals to [" + actual + "]";
            throw new AssertionException(errorMessage);
        }

        if (actual != null && expected != null) {
            if (actual == expected || actual.equals(expected)) {
                String errorMessage = "Assert Not Equals: " + message + ", expected [" + expected + "] equals to [" + actual + "]";
                throw new AssertionException(errorMessage);
            }
        }
    }

    public static void checkNotNull(Object var0, String var1) {
        if (var0 == null) {
            String var2 = "";
            if (var1 != null) {
                var2 = var1 + " ";
            }
            throw new AssertionException(var2 + "expected object to not be null");
        }
    }

    public static void checkFail(String message) {
        String errorMessage = "Assert Fail: " + message;
        throw new AssertionException(errorMessage);
    }
}
