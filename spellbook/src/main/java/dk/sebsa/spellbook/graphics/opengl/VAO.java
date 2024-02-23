package dk.sebsa.spellbook.graphics.opengl;

import lombok.Getter;

import static org.lwjgl.opengl.GL30.*;

/**
 * This class represents a Vertex Array Object (VAO).
 *
 * @author sebs
 * @since 1.0.0
 */
@Getter
public class VAO {
    /**
     * VAO's id
     */
    private final int id;

    /**
     * Creates a Vertex Array Object (VAO).
     */
    public VAO() {
        id = glGenVertexArrays();
    }

    /**
     * Binds the VAO.
     */
    public void bind() {
        glBindVertexArray(id);
    }

    /**
     * Deletes the VAO.
     */
    public void destroy() {
        glDeleteVertexArrays(id);
    }

    /**
     * Binds the default VAO
     */
    public void unbind() {
        glBindVertexArray(0);
    }
}