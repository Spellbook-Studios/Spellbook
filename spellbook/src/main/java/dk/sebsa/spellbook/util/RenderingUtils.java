package dk.sebsa.spellbook.util;

import dk.sebsa.spellbook.math.Rect;
import org.joml.Matrix4f;

/**
 * Utility functions for rendering
 *
 * @since 1.0.0
 * @author sebs
 */
public class RenderingUtils {
    /**
     * Generates a projection matrix with 0,0 centered in the middle of the screen
     *
     * @param r The bounds of the matrix (width and height are used)
     * @return JOML Matrix 4f
     */
    public static Matrix4f centeredOrthoMatrix(Rect r) {
        float w = r.width;
        float h = r.height;
        float halfW = w * 0.5f;
        float halfH = h * 0.5f;

        return new Matrix4f().ortho(-halfW, halfW, halfH, -halfH, -1, 1);
    }
}
