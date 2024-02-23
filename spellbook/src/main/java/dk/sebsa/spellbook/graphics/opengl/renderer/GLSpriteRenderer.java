package dk.sebsa.spellbook.graphics.opengl.renderer;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.graphics.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.graphics.opengl.Material;
import dk.sebsa.spellbook.graphics.opengl.VAO;
import dk.sebsa.spellbook.graphics.opengl.VBO;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.util.RenderingUtils;
import lombok.Getter;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

/**
 * Renders 2D objects in batches
 *
 * @since 1.0.0
 * @author sebs
 */
public class GLSpriteRenderer {
    @Getter
    private final GLSLShaderProgram shader;
    private final VBO vbo;
    private final FloatBuffer vertices;
    private final VAO vao;
    private int numVertices = 0;

    /**
     * A GLBatchRenderer using the provided shader
     * @param shaderI Identifier for a shader that has:
     *                Uniforms = [vec4 matColor, sampler2d texSampler, mat4 mProj, mat4 mView]
     *                layout (location=0) in vec2 x;
     *                layout (location=1) in vec2 y;
     *
     */
    public GLSpriteRenderer(Identifier shaderI) {
        shader = (GLSLShaderProgram) AssetManager.getAssetS(shaderI);
        shader.createUniform("matColor");
        shader.createUniform("texSampler");
        shader.createUniform("mProj");
        shader.createUniform("mView");

        // Gen the VAO and VBO
        vao = new VAO(); vao.bind();
        vbo = new VBO(); vbo.bind(GL_ARRAY_BUFFER);
        vertices = MemoryUtil.memAllocFloat(4096);
        long size = (long) vertices.capacity() * Float.BYTES;
        vbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        vao.unbind();
    }

    /**
     * Makes OpenGL ready for rendering, using a centered projection
     *
     * @param r Rect to render two (used to generate projection)
     */
    public void begin(Rect r) {
        begin(RenderingUtils.centeredOrthoMatrix(r));
    }

    /**
     * Makes OpenGL ready for rendering
     *
     * @param mProj Projection Matrix
     */
    public void begin(Matrix4f mProj) {
        glDisable(GL_DEPTH_TEST);
        shader.bind();
        shader.setUniform("mProj", mProj);
        shader.setUniform("mView", Camera.activeCamera.getViewMatrix());

        vao.bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
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
     * Renderes all vertices.
     * This is already done automatically,
     * but this function might still be useful if using specific GL modes (e.g. GL_LINE_LOOP)
     */
    public void flush() {
        if(numVertices > 0) {
            vertices.flip();
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);
            glDrawArrays(GL_TRIANGLES, 0, numVertices);
            vertices.clear();
            numVertices = 0;
        }
    }

    /**
     * Flushes all data and unbinds GL resources
     */
    public void end() {
        flush();
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        vao.unbind();
        shader.unbind();
    }

    /**
     * @param rect   Object rect
     * @param uv     Object uvRect
     */
    public void drawQuad(Rect rect, Rect uv) {
        if (vertices.remaining() < Float.BYTES * 4) flush();
        float x1 = rect.x;
        float x2 = rect.x + rect.width;
        float y1 = rect.y;
        float y2 = rect.y + rect.height;

        float s1 = uv.x;
        float s2 = uv.x + uv.width;
        float t1 = uv.y;
        float t2 = uv.y + uv.height;

        vertices.put(x1).put(y2).put(s1).put(t2);
        vertices.put(x2).put(y2).put(s2).put(t2);
        vertices.put(x2).put(y1).put(s2).put(t1);

        vertices.put(x2).put(y1).put(s2).put(t1);
        vertices.put(x1).put(y1).put(s1).put(t1);
        vertices.put(x1).put(y2).put(s1).put(t2);

        numVertices += 6;
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
