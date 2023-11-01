package dk.sebsa.spellbook.math;

import lombok.ToString;

/**
 * A vector of 2 floats
 *
 * @author sebs
 * @since 1.0.0
 */
@ToString
public class Vector2f {
    /**
     * x value of vector
     */
    public float x;
    /**
     * y value of vector
     */
    public float y;

    /**
     * A vector which is always (1.0f, 1.0f)
     */
    public static final Vector2f VECTOR2F_ONE = new Vector2f(1, 1);

    /**
     * A new vector with the same value as V
     *
     * @param v Values
     */
    public Vector2f(Vector2f v) {
        x = v.x;
        y = v.y;
    }

    /**
     * @param x x value
     * @param y y value
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param x x value
     * @param y y value
     */
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
     *
     * @param v New Values
     * @return this
     */
    public Vector2f set(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    /**
     * Sets this vectors values to (x, y)
     *
     * @param x New x
     * @param y New y
     * @return this
     */
    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Returns a new vector equal to (this.x + v, this.y + v)
     *
     * @param v Value to add to
     * @return New Vector
     */
    public Vector2f add(float v) {
        return new Vector2f(x + v, y + v);
    }

    /**
     * Returns a new vector equal to (this.x + v.x, this.y + v.y)
     *
     * @param v Vector to add to
     * @return New Vector
     */
    public Vector2f add(Vector2f v) {
        return new Vector2f(x + v.x, y + v.y);
    }

    /**
     * Returns a new vector equal to (x + this.x, y + this.y)
     *
     * @param x x value to add to
     * @param y y value to add to
     * @return New Vector
     */
    public Vector2f add(float x, float y) {
        return new Vector2f(x + this.x, y + this.y);
    }

    /**
     * Returns a new vector equal to (this.x - v, this.y - v)
     *
     * @param v Value to subtract by
     * @return New Vector
     */
    public Vector2f sub(float v) {
        return new Vector2f(x - v, y - v);
    }

    /**
     * Returns a new vector equal to (this.x - v.x, this.y - v.y)
     *
     * @param v Vector to subtract by
     * @return New Vector
     */
    public Vector2f sub(Vector2f v) {
        return new Vector2f(x - v.x, y - v.y);
    }

    /**
     * Returns a new vector equal to (this.x - x , this.y - y)
     *
     * @param x x value to subtract by
     * @param y y value to subtract by
     * @return New Vector
     */
    public Vector2f sub(float x, float y) {
        return new Vector2f(this.x - x, this.y - y);
    }

    /**
     * Returns a new vector equal to (this.x/v, this.y/v)
     *
     * @param v Value to divide by
     * @return New Vector
     */
    public Vector2f div(float v) {
        return new Vector2f(x / v, y / v);
    }

    /**
     * Returns a new vector equal to (this.x/v.x, this.y/v.y)
     *
     * @param v Vector to divide by
     * @return New Vector
     */
    public Vector2f div(Vector2f v) {
        return new Vector2f(x / v.x, y / v.y);
    }

    /**
     * Returns a new vector equal to (this.x/x, this.y/y)
     *
     * @param x x value to divide by
     * @param y y value to divide by
     * @return New Vector
     */
    public Vector2f div(float x, float y) {
        return new Vector2f(this.x / x, this.y / y);
    }

    /**
     * Returns a new vector equal to (this.x*v, this.y*v)
     *
     * @param v Value to multiply with
     * @return New Vector
     */
    public Vector2f mul(float v) {
        return new Vector2f(x * v, y * v);
    }

    /**
     * Returns a new vector equal to (this.x*v.x, this.y*v.y)
     *
     * @param v Vector to multiply with
     * @return New Vector
     */
    public Vector2f mul(Vector2f v) {
        return new Vector2f(x * v.x, y * v.y);
    }

    /**
     * Returns a new vector equal to (this.x*x, this.y*y)
     *
     * @param x x value to multiply with
     * @param y y value to multiply with
     * @return New Vector
     */
    public Vector2f mul(float x, float y) {
        return new Vector2f(x * this.x, y * this.y);
    }

    /**
     * @return The lowest of the vectors values
     */
    public float min() {
        return Math.min(x, y);
    }

    /**
     * @return The highest of the vectors values
     */
    public float max() {
        return Math.max(x, y);
    }

    /**
     * Normalizes this vector and returns it in a new vector
     *
     * @return Another vector equal to this normalized
     */
    public Vector2f normalize() {
        float invLength = 1.0f / (float) java.lang.Math.sqrt(x * x + y * y);
        return new Vector2f(x * invLength, y * invLength);
    }
}
