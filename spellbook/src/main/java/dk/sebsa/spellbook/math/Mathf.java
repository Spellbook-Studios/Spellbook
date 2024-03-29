package dk.sebsa.spellbook.math;

/**
 * Utils for doing math with floats
 *
 * @author sebs
 * @since 1.0.0
 */
public class Mathf {
    /**
     * Wraps a value to be within the min and max values
     *
     * @param val Value to wrap
     * @param min Minimum value
     * @param max Max value (Result is always lower)
     * @return The wrapped value
     */
    public static float wrap(float val, float min, float max) {
        float remainder = max - min;
        return ((val - min) % remainder + remainder) % remainder + min;
    }

    /**
     * Clamps a value to be within the min and max values
     *
     * @param val Value to clamp
     * @param min Minimum value
     * @param max Max value
     * @return The clamped value
     */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Returns the square distance between 2 points in 2d space
     *
     * @param a Point a
     * @param b Point B
     * @return The distance between the points squared
     */
    public static float sqDistance(Vector2f a, Vector2f b) {
        float x = a.x - b.x;
        float y = a.y - b.y;
        return x * x + y * y;
    }
}
