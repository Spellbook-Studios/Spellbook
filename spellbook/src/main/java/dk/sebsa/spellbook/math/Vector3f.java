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
     * Returns a new vector equal to (this.x + v, this.y + v, this.z + v)
     * @param v Value to add to
     * @return New Vector
     */
    public Vector3f add(float v) { return new Vector3f(x + v, y + v, z + v); }
    /**
     * Returns a new vector equal to (this.x + v.x, this.y + v.y, this.z + v.z)
     * @param v Vector to add to
     * @return New Vector
     */
    public Vector3f add(Vector3f v) { return new Vector3f(x + v.x, y + v.y, z + v.z); }
    /**
     * Returns a new vector equal to (x + this.x, y + this.y, z + this.z)
     * @param x x value to add to
     * @param y y value to add to
     * @param z z value to add to
     * @return New Vector
     */
    public Vector3f add(float x, float y, float z) { return new Vector3f(x + this.x, y + this.y, z + this.z); }
    
    /**
     * Returns a new vector equal to (this.x - v, this.y - v, this.z - v)
     * @param v Value to subtract by
     * @return New Vector
     */
    public Vector3f sub(float v) { return new Vector3f(x - v, y - v, z - v); }
    /**
     * Returns a new vector equal to (this.x - v.x, this.y - v.y, this.z - v.z)
     * @param v Vector to subtract by
     * @return New Vector
     */
    public Vector3f sub(Vector3f v) { return new Vector3f(x - v.x, y - v.y, z - v.z); }
    /**
     * Returns a new vector equal to (this.x - x , this.y - y, this.z - z)
     * @param x x value to subtract by
     * @param y y value to subtract by
     * @param z z value to subtract by
     * @return New Vector
     */
    public Vector3f sub(float x, float y, float z) { return new Vector3f(this.x - x, this.y - y, this.z - z); }

    /**
     * Returns a new vector equal to (this.x/v, this.y/v, this.z/v)
     * @param v Value to divide by
     * @return New Vector
     */
    public Vector3f div(float v) { return new Vector3f(x / v, y / v, z / v); }
    /**
     * Returns a new vector equal to (this.x/v.x, this.y/v.y, this.z/v.z)
     * @param v Vector to divide by
     * @return New Vector
     */
    public Vector3f div(Vector3f v) { return new Vector3f(x / v.x, y / v.y, z / v.z); }
    /**
     * Returns a new vector equal to (this.x/x, this.y/y, this.z/z)
     * @param x x value to divide by
     * @param y y value to divide by
     * @param z z value to divide by
     * @return New Vector
     */
    public Vector3f div(float x, float y, float z) { return new Vector3f(this.x / x, this.y / y, this.z / z); }

    /**
     * Returns a new vector equal to (this.x*v, this.y*v, this.z*v)
     * @param v Value to multiply with
     * @return New Vector
     */
    public Vector3f mul(float v) { return new Vector3f(x * v, y * v, z * v); }
    /**
     * Returns a new vector equal to (this.x*v.x, this.y*v.y, this.z*v.z)
     * @param v Vector to multiply with
     * @return New Vector
     */
    public Vector3f mul(Vector3f v) { return new Vector3f(x * v.x, y * v.y, z * v.z); }
    /**
     * Returns a new vector equal to (this.x*x, this.y*y, this.z*z)
     * @param x x value to multiply with
     * @param y y value to multiply with
     * @param z z value to multiply with
     * @return New Vector
     */
    public Vector3f mul(float x, float y, float z) { return new Vector3f(x * this.x, y * this.y, z * this.z); }

    /**
     * Weather all values are 0
     * @return True if all values equal 0.0f, false otherwise
     */
    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }
}
