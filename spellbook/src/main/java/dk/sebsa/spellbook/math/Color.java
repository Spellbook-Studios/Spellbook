package dk.sebsa.spellbook.math;

import dk.sebsa.spellbook.util.FourKeyHashMap;

/**
 * A representation of an RGBA color
 *
 * @author sebs
 * @since 1.0.0
 */
public class Color {
    /**
     * Red value of color
     */
    public final float r;
    /**
     * Green value of color
     */
    public final float g;
    /**
     * Blue value of color
     */
    public final float b;
    /**
     * Alpha value of color
     */
    public final float a;

    private static final FourKeyHashMap<Float, Float, Float, Float, Color> colorPool = new FourKeyHashMap<>();

    /**
     * Returns the color with the specified value (and alpha of 1.0f)
     *
     * @param r Red Value
     * @param g Green Value
     * @param b Blue Value
     * @return The color matching the specified values
     */
    public static Color color(float r, float g, float b) {
        return color(r, g, b, 1);
    }

    /**
     * Returns the color with the specified value
     *
     * @param r Red Value
     * @param g Green Value
     * @param b Blue Value
     * @param a Alpha Value
     * @return The color matching the specified values
     */
    public static Color color(float r, float g, float b, float a) {
        return colorPool.getPut(a, r, g, b, () -> new Color(r, g, b, a));
    }

    private Color(float r, float g, float b, float a) {
        this.r = Mathf.clamp(r, 0, 1);
        this.b = Mathf.clamp(b, 0, 1);
        this.g = Mathf.clamp(g, 0, 1);
        this.a = Mathf.clamp(a, 0, 1);
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }

    // Defaults
    /**
     * Color
     * R=0, G=0, B=0, A=255
     */
    public static final Color black = color(0, 0, 0);
    /**
     * Color
     * R=255, G=255, B=255, A=255
     */
    public static final Color white = color(1, 1, 1);
    /**
     * Color
     * R=255, G=0, B=0, A=255
     */
    public static final Color red = color(1, 0, 0);
    /**
     * Color
     * R=0, G=255, B=0, A=255
     */
    public static final Color green = color(0, 1, 0);
    /**
     * Color
     * R=0, G=0, B=255, A=255
     */
    public static final Color blue = color(0, 0, 1);
    /**
     * Color
     * R=127, G=127, B=127, A=255
     */
    public static final Color grey = color(0.5f, 0.5f, 0.5f);
    /**
     * Color
     * R=89, G=89, B=89, A=255
     */
    public static final Color dimGrey = color(0.35f, 0.35f, 0.35f);
    /**
     * Color
     * R=76, G=76, B=76, A=255
     */
    public static final Color darkGrey = color(0.3f, 0.3f, 0.3f);
    /**
     * Color
     * R=127, G=0, B=0, A=255
     */
    public static final Color wine = color(0.5f, 0, 0);
    /**
     * Color
     * R=0, G=127, B=0, A=255
     */
    public static final Color forest = color(0, 0.5f, 0);
    /**
     * Color
     * R=0, G=0, B=127, A=255
     */
    public static final Color marine = color(0, 0, 0.5f);
    /**
     * Color
     * R=255, G=255, B=0, A=255
     */
    public static final Color yellow = color(1, 1, 0);
    /**
     * Color
     * R=0, G=255, B=255, A=255
     */
    public static final Color cyan = color(0, 1, 1);
    /**
     * Color
     * R=255, G=0, B=255, A=255
     */
    public static final Color magenta = color(1, 0, 1);
    /**
     * Color
     * R=0, G=0, B=0, A=0
     */
    public static final Color transparent = color(0, 0, 0, 0);
    /**
     * Color
     * R=255, G=160, B=0, A=255
     */
    public static final Color neonOrange = color(1, 0.6470588f, 0.0f);
}
