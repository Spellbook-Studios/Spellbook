package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Matrix4x4f;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import org.lwjgl.opengl.GL30;


import static org.lwjgl.opengl.GL11.*;

public class Sprite2D {
    private static Mesh2D mainMesh;
    private static AssetReference shaderR;
    private static GLSLShaderProgram shader;

    public static void init(EngineLoadEvent e) {
        if(mainMesh != null) return;

        mainMesh = Mesh2D.getRenderMesh();

        // Get shader
        shaderR = e.assetManager.getAsset("/spellbook/shaders/Sprite2D.glsl");
        shader = shaderR.get();

        // Prepare shader
        try {
            //shader.createUniform("transformMatrix", e.logger);
            shader.createUniform("pixelScale", e.logger);
            shader.createUniform("objectScale", e.logger);
            shader.createUniform("anchor", e.logger);
            shader.createUniform("offset", e.logger);
            shader.createUniform("projection", e.logger);

            shader.createUniform("matColor", e.logger);
        } catch (Exception ex) {
            Spellbook.instance.error(new ClassLogger(new Sprite2D(), e.logger).stackTrace(ex), true);
        }
    }

    public static void renderSprite(GLFWWindow window, Rect r, SpriteRenderer sprite) {
        glDisable(GL_DEPTH_TEST);

        // Projection matrix
        float w = r.width;
        float h = r.height;
        float halfW = w * 0.5f;
        float halfH = h * 0.5f;

        Matrix4x4f projection = Matrix4x4f.ortho(-halfW, halfW, halfH, -halfH, -1, 1);

        // Bind
        mainMesh.bind();
        shader.bind();
        shader.setUniform("projection", projection);
        sprite.getSprite().getMaterial().bind(shader);

        // Render
        sprite.setUniforms(shader);
        GL30.glDrawArrays(GL_TRIANGLES, 0, 6);

        // Restore
        sprite.getSprite().getMaterial().unbind();
        shader.unbind();
        mainMesh.unbind();
        glEnable(GL_DEPTH_TEST);
    }

    public static void destroy() {
        shader = null;
        shaderR.unRefrence();
    }
}
