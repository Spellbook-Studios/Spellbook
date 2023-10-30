package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import java.util.Collection;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Renders 2d sprites
 *
 * @author sebs
 * @since 1.0.0
 */
public class Sprite2D {
    private static Mesh2D mainMesh;
    private static AssetReference shaderR;
    private static GLSLShaderProgram shader;

    /**
     * Inits the renderer
     *
     * @param e The engineLoadEvent
     */
    public static void init(EngineLoadEvent e) {
        if (mainMesh != null) return;

        mainMesh = Mesh2D.getRenderMesh();

        // Get shader
        shaderR = e.assetManager.getAsset("/spellbook/shaders/Sprite2D.glsl");
        shader = shaderR.get();

        // Prepare shader
        try {
            shader.createUniform("transformMatrix", e.logger);
            shader.createUniform("pixelScale", e.logger);
            shader.createUniform("objectScale", e.logger);
            shader.createUniform("anchor", e.logger);
            shader.createUniform("offset", e.logger);
            shader.createUniform("projectionViewMatrix", e.logger);

            shader.createUniform("matColor", e.logger);
        } catch (Exception ex) {
            //noinspection InstantiationOfUtilityClass
            Spellbook.instance.error(new ClassLogger(new Sprite2D(), e.logger).stackTrace(ex), true);
        }
    }

    /**
     * Renders a list of spriterenders to the screen
     *
     * @param window    The window to render to
     * @param r         Where to render to
     * @param frameData The spriterenders to render
     */
    public static void renderSprites(GLFWWindow window, Rect r, FrameData frameData) {
        glDisable(GL_DEPTH_TEST);

        // Projection matrix
        float w = r.width;
        float h = r.height;
        float halfW = w * 0.5f;
        float halfH = h * 0.5f;

        Matrix4f projection = new Matrix4f().ortho(-halfW, halfW, halfH, -halfH, -1, 1);

        // Bind
        mainMesh.bind();
        shader.bind();
        shader.setUniform("projectionViewMatrix", projection.mul(Camera.activeCamera.getViewMatrix()));

        for (int i = 0; i < frameData.getRenderSprite().length; i++) {
            Map<Sprite, Collection<SpriteRenderer>> layer = frameData.getRenderSprite()[i];
            for (Sprite s : layer.keySet()) {
                s.getMaterial().bind(shader);

                for (SpriteRenderer sr : layer.get(s)) {
                    // Render
                    sr.setUniforms(shader);
                    GL30.glDrawArrays(GL_TRIANGLES, 0, 6);
                }

                s.getMaterial().unbind();
            }
        }

        // Restore
        shader.unbind();
        mainMesh.unbind();
        glEnable(GL_DEPTH_TEST);
    }

    /**
     * Cleanup assets used by Sprite2D
     */
    public static void destroy() {
        shader = null;
        shaderR.unReference();
    }
}
