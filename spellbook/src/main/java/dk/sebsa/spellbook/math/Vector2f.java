package dk.sebsa.spellbook.math;

/**
 * A vector of 2 floats
 * @author sebsn
 * @since 0.0.1
 */
public class Vector2f {
    public float x;
    public float y;

    /**
     * A vector which is always (1.0f, 1.0f)
     */
    public static final Vector2f VECTOR2F_ONE = new Vector2f(1,1);

    /**
     * A new vector with the same value as V
     */
    public Vector2f(Vector2f v) {
        x = v.x; y = v.y;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * A vector which values are (0.0f, 0.0f)
     */
    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Sets values to (0.0f, 0.0f)
     */
    public void zero() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Set this vectors values to the values of V
     * @return this
     */
    public Vector2f set(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    /**
     * Sets this vectors values to (x, y)
     * @return this
     */
    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }



    public Vector2f add(float v) { return new Vector2f(x + v, y + v); }
    public Vector2f add(Vector2f v) { return new Vector2f(x + v.x, y + v.y); }
    public Vector2f add(float x, float y) { return new Vector2f(x + this.x, y + this.y); }

    public Vector2f sub(float v) { return new Vector2f(x - v, y - v); }
    public Vector2f sub(Vector2f v) { return new Vector2f(x - v.x, y - v.y); }
    public Vector2f sub(float x, float y) { return new Vector2f(x - this.x, y - this.y); }

    public Vector2f div(float v) { return new Vector2f(x / v, y / v); }
    public Vector2f div(Vector2f v) { return new Vector2f(x / v.x, y / v.y); }
    public Vector2f div(float x, float y) { return new Vector2f(x / this.x, y / this.y); }

    public Vector2f mul(float v) { return new Vector2f(x * v, y * v); }
    public Vector2f mul(Vector2f v) { return new Vector2f(x * v.x, y * v.y); }
    public Vector2f mul(float x, float y) { return new Vector2f(x * this.x, y * this.y); }

    /**
     * @return The lowest of the vectors values
     */
    public float min() { return Math.min(x,y); }

    /**
     * @return The highest of the vectors values
     */
    public float max() { return Math.max(x,y); }

    @Override
    public String toString() {
        return "Vector2f{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
