package dk.sebsa.spellbook.debug;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL42.*;

/**
 * A vertex array object, that can draw itself to the creen
 * @author sebs
 * @since 1.0.0
 */
public class DebugVAO {
    private final int vertexSize;
    private final int vboVertexID;
    private final int vaoID;
    private int amount;

    /**
     * @param vertices Verticies
     * @param vertexSize The size of each vertex e.g. 2 for lines
     */
    public DebugVAO(float[] vertices, int vertexSize) {
        this.vertexSize = vertexSize;

        // Vertex
        FloatBuffer vertex_data = MemoryUtil.memAllocFloat(vertices.length);
        vertex_data.put(vertices);
        vertex_data.flip();

        // VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboVertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertex_data, GL_STATIC_DRAW);
        glVertexAttribPointer(0, vertexSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
        MemoryUtil.memFree(vertex_data);
        amount = vertices.length/vertexSize;
    }

    /**
     * Outs verticies into VAO
     * @param vertices Verticies to add
     */
    public void put(float[] vertices) {
        FloatBuffer vertex_data = MemoryUtil.memAllocFloat(vertices.length);
        vertex_data.put(vertices);
        vertex_data.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertex_data, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        amount = vertices.length/vertexSize;
    }

    /**
     * Draws the VAO to the screen
     * @param mode The GL drawing mode to use e.g. GL_LINES
     */
    public void draw(int mode) {
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);

        glDrawArrays(mode, 0, amount);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    /**
     * Binds the VAO and enables attributes
     */
    public void bind() {
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
    }

    /**
     * Binds the VAO and enables attributes
     */
    public void unbind() {
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
    }

    /**
     * Destroys the VAO object
     */
    public void destroy() {
        glDeleteBuffers(vboVertexID);
        glDeleteVertexArrays(vaoID);
    }
}
