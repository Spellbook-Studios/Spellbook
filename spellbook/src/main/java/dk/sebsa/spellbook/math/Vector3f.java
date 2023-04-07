package dk.sebsa.spellbook.math;

/**
 * A vector of 3 floats
 * @author sebsn
 * @since 0.0.1
 */
public class Vector3f {
    /**
     * x Value
     */
    public float x;
    /**
     * y Value
     */
    public float y;
    /**
     * z Value
     */
    public float z;

    /**
     * A new vector with the same value as V
     * @param v Values
     */
    public Vector3f(Vector3f v) {
        x = v.x; y = v.y; z = v.z;
    }

    /**
     * @param x x value
     * @param y y value
     * @param z z value
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @param x x value
     * @param y y value
     * @param z z value
     */
    public Vector3f(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }

    /**
     * A vector which values are (0.0f, 0.0f, 0.0f)
     */
    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Sets values to (0.0f, 0.0f, 0.0f)
     */
    public void zero() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Set this vectors values to the values of V
     * @param v New Values
     * @return this
     */
    public Vector3f set(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    /**
     * Sets this vectors values to (x, y, z)
     * @param x New x
     * @param y New y
     * @param z New z
     * @return this
     */
    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public String toString() {
        return "Vector3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    /**
     * Adds values of a vector to this vector
     * @param v Vector to add to this
     * @return this
     */
    public Vector3f add(Vector3f v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }
}
