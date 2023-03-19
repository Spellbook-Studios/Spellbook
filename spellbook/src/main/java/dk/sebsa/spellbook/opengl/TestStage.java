package dk.sebsa.spellbook.opengl;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.io.GLFWWindow;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class TestStage extends RenderStage {
    private final int vaoID, vboID;
    private AssetReference shaderR;
    private GLSLShaderProgram shader;

    public TestStage(AssetManager assetManager, GLFWWindow window) {
        super(window);
        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        float[] vertices = new float[]
                {
                        +0.0f, +0.8f,    // Top coordinate
                        -0.8f, -0.8f,    // Bottom-left coordinate
                        +0.8f, -0.8f     // Bottom-right coordinate
                };

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);


        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);

        shaderR = assetManager.getAsset("/spellbook/shaders/test.glsl");
        shader = shaderR.get();
    }

    @Override
    public String getName() {
        return "OpenGLTestStage";
    }

    @Override
    protected void draw(FBO prevFBO) {
        shader.bind();
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);

        // Draw a triangle of 3 vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Disable our location
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shader.unbind();
    }

    @Override
    public void destroy() {
        shader = null;
        shaderR.unRefrence();
        // Dispose the vertex array
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);

        // Dispose the buffer object
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
    }
}
