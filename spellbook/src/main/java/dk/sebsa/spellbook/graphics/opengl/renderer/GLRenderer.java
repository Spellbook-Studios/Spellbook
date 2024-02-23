package dk.sebsa.spellbook.graphics.opengl.renderer;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.graphics.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.graphics.opengl.Material;
import dk.sebsa.spellbook.graphics.opengl.VAO;
import dk.sebsa.spellbook.graphics.opengl.VBO;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.util.RenderingUtils;
import lombok.Getter;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

/**
 * Renders 2D objects in batches
 *
 * @since 1.0.0
 * @author sebs
 */
public class GLRenderer {
    @Getter
    private final GLSLShaderProgram shader;
    private final VBO vbo;
    private final FloatBuffer vertices;
    private final VAO vao;
    private int mode = GL_TRIANGLES;
    private int numVertices = 0;

    /**
     * A GLBatchRenderer using the provided shader
     * @param shaderI Identifier for a shader that has:
     *                Uniforms = [vec4 matColor, mat4 mProj, mat4 mView]
     *                layout (location=0) in vec2 x;
     *
     */
    public GLRenderer(Identifier shaderI) {
        shader = (GLSLShaderProgram) AssetManager.getAssetS(shaderI);
        shader.createUniform("matColor");
        shader.createUniform("mProj");
        shader.createUniform("mView");

        // Gen the VAO and VBO
        vao = new VAO(); vao.bind();
        vbo = new VBO(); vbo.bind(GL_ARRAY_BUFFER);
        vertices = MemoryUtil.memAllocFloat(4096);
        long size = (long) vertices.capacity() * Float.BYTES;
        vbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        vao.unbind();
    }

    /**
     * Makes OpenGL ready for rendering
     *
     * @param r Rect to render two (used to generate projection)
     */
    public void begin(Rect r) {
        glDisable(GL_DEPTH_TEST);
        shader.bind();
        shader.setUniform("mProj", RenderingUtils.centeredOrthoMatrix(r));
        shader.setUniform("mView", Camera.activeCamera.getViewMatrix());

        vao.bind();
        glEnableVertexAttribArray(0);
    }

    /**
     * Flushes current data and sets the material
     * @param material Material to render with
     */
    public void setMaterial(Material material) {
        flush();
        material.bind(shader);
    }

    /**
     * Flushes the current vertices and sets the OpenGL mode
     *
     * @param mode Sets the mode (GL_TRIANGLES, GL_LINES)
     */
    public void setMode(int mode) {
        flush();
        this.mode = mode;
    }

    /**
     * Renderes all vertices.
     * This is already done automatically,
     * but this function might still be useful if using specific GL modes (e.g. GL_LINE_LOOP)
     */
    public void flush() {
        if(numVertices > 0) {
            vertices.flip();
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);
            glDrawArrays(mode, 0, numVertices);
            vertices.clear();
            numVertices = 0;
        }
    }

    /**
     * Flushes all data and unbinds GL resources
     */
    public void end() {
        flush();
        glDisableVertexAttribArray(0);
        vao.unbind();
        shader.unbind();
    }

    /**
     * Adds a single vertex to the buffer
     * @param point Point position
     */
    public void drawPoint(Vector2f point) {
        drawPoint(point.x, point.y);
    }

    /**
     * Adds a single vertex to the buffer
     * @param x Point x position
     * @param y Point y position
     */
    public void drawPoint(float x, float y) {
        vertices.put(x).put(y);
        numVertices += 1;
    }

    /**
     * Adds two vertexs to the buffer
     * @param a Point A pos
     * @param b Point B pos
     */
    public void drawLine(Vector2f a, Vector2f b) {
        drawLine(a.x,a.y,b.x,b.y);
    }

    /**
     * Adds two vertexs to the buffer
     * @param x1 Point A x position
     * @param y1 Point A y position
     * @param x2 Point B x position
     * @param y2 Point B x position
     */
    public void drawLine(float x1, float y1, float x2, float y2) {
        vertices.put(x1).put(y1);
        vertices.put(x2).put(y2);
        numVertices += 2;
    }

    /**
     * Unreferences and cleans up resources
     */
    public void destroy() {
        shader.unreference();
        vbo.destroy();
        vao.destroy();
    }
}
