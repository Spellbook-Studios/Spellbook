package dk.sebsa.spellbook.graphics.opengl;

import lombok.Getter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * This class represents a Vertex Buffer Object (VBO).
 *
 * @author sebs
 * @since 1.0.0
 */
@Getter
public class VBO {
    /**
     * VBO's id
     */
    private final int id;

    /**
     * Creates a Vertex Buffer Object (VBO).
     */
    public VBO() {
        id = glGenBuffers();
    }

    /**
     * Binds this VBO with specified target.
     *
     * @param target Target to bind to
     */
    public void bind(int target) {
        glBindBuffer(target, id);
    }

    /**
     * Upload vertex data to this VBO with specified target, data and usage.
     *
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public void uploadData(int target, FloatBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Upload null data to this VBO with specified target, size and usage.
     *
     * @param target Target to upload
     * @param size   Size in bytes of the VBO data store
     * @param usage  Usage of the data
     */
    public void uploadData(int target, long size, int usage) {
        glBufferData(target, size, usage);
    }

    /**
     * Upload sub data to this VBO with specified target, offset and data.
     *
     * @param target Target to upload
     * @param offset Offset where the data should go in bytes
     * @param data   Buffer with the data to upload
     */
    public void uploadSubData(int target, long offset, FloatBuffer data) {
        glBufferSubData(target, offset, data);
    }

    /**
     * Upload element data to this VBO with specified target, data and usage.
     *
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public void uploadData(int target, IntBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Destroys the VBO
     */
    public void destroy() {
        glDeleteBuffers(id);
    }

}
