package dk.sebsa.spellbook.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Functions for generating pseudo random values in a thread safe way
 *
 * @author sebsn
 * @since 1.0.0
 */
public class Random {
    /**
     * @param min The minimum value of the int (inclusive)
     * @param max The maximum value of that int (exclusive)
     * @return a random int within the range specified
     */
    public static int getInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * @return a random int
     */
    public static int getInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    /**
     * @return either true or false
     */
    public static boolean getBool() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /**
     * @return a random float within 0.0f and 1.0f
     */
    public static float getFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    /**
     * @param min Lowest possible output value
     * @param max Highest possible output value
     * @return a random float within min and max
     */
    public static float getFloat(float min, float max) {
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }
}
