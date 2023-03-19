package dk.sebsa.spellbook.math;

import java.nio.FloatBuffer;

/**
 * a 4 x 4 matrix with floats
 * @author sebs
 * @since 0.0.1
 */
public class Matrix4x4f {
    private final float[][] m = new float[4][4];

    public Matrix4x4f() {
        setIdentity();
    }

    public final void setIdentity() {
        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = 0;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;
    }

    public void getBuffer(FloatBuffer buffer) {
        buffer.put(m[0][0]).put(m[0][1]).put(m[0][2]).put(m[0][3]);
        buffer.put(m[1][0]).put(m[1][1]).put(m[1][2]).put(m[1][3]);
        buffer.put(m[2][0]).put(m[2][1]).put(m[2][2]).put(m[2][3]);
        buffer.put(m[3][0]).put(m[3][1]).put(m[3][2]).put(m[3][3]);
        buffer.flip();
    }

    public static Matrix4x4f ortho(float left, float right, float bottom, float top, float near, float far) {
        Matrix4x4f matrix = new Matrix4x4f();

        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        matrix.m[0][0] = 2f/width;
        matrix.m[1][1] = 2f/height;
        matrix.m[2][2] = 2f/depth;

        matrix.m[3][0] = -(right+left) / width;
        matrix.m[3][1] = -(top+bottom) / height;
        matrix.m[3][2] = -(far+near) / depth;

        return matrix;
    }

    public void setTransformation(Vector2f p, float r, Vector2f s) {
        float radians = (float) Math.toRadians(r);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        m[0][0] = cos * s.x; m[0][1] = sin * s.y;
        m[1][0] = -sin * s.x; m[1][1] = cos * s.y;
        if(p != null) {m[0][3] = p.x; m[1][3] = p.y;}
    }

    public Vector2f transformPoint(Vector2f v) {
        Vector2f p = new Vector2f();
        p.x = m[0][3] + (m[0][0] * v.x + -m[1][0] * v.y);
        p.y = m[1][3] + (-m[0][1] * v.x + m[1][1] * v.y);
        return p;
    }
}
