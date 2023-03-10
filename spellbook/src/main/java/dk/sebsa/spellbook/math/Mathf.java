package dk.sebsa.spellbook.math;

/**
 * Utils for doing math with floats
 * @author sebsn
 * @since 0.0.1
 */
public class Mathf {
    /**
     * Wraps a value to be within the min and max values
     * @param val
     * @param min
     * @param max
     * @return The wrapped value
     */
    public static float wrap(float val, float min, float max) {
        float remainder = max - min;
        return ((val-min) % remainder + remainder) % remainder + min;
    }

    /**
     * Clamps a value to be within the min and max values
     * @param val
     * @param min
     * @param max
     * @return The clamped value
     */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
