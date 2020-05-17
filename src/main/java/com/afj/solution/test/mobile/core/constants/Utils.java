package com.afj.solution.test.mobile.core.constants;

import java.util.Random;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Tomash Gombosh
 */
@Log4j
public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Suppress default constructor for noninstantiability");
    }

    /**
     * Waits for a moment.
     *
     * @param millis how many milliseconds to sleep.
     */
    public static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.info(String.format("Exception %s", e.getMessage()));
        }
    }

    /**
     * Generates a random string.
     *
     * @param length how many characters the string should be.
     * @return a randomly generated string `length` characters long.
     */
    public static String getRandomString(final int length) {
        return RandomStringUtils.randomAlphabetic(length).toLowerCase();
    }

    /**
     * Gets a random integer.
     */
    public static int getRandomInteger(final int max) {
        final Random randomGenerator = new Random();
        return randomGenerator.nextInt(max);
    }

    public static String getOs() {
        return System.getProperty("os.name");
    }
}
