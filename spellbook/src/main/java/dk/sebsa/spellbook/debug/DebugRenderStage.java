package dk.sebsa.spellbook.debug;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.RenderStage;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;
import dk.sebsa.spellbook.phys.components.CircleCollider2D;
import dk.sebsa.spellbook.phys.components.Collider2D;
import lombok.Getter;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

/**
 * Renders debug objects to the screen, such as colliders
 * DO NOT USE IN PROD
 *
 * @author sebs
 * @since 1.0.0
 */
public class DebugRenderStage extends RenderStage {
    private final float[] testPos = {
            0.0f, 0.0f,
            10, 0.0f,
            0.0f, 0.0f,
            -10, 0.0f,
    };
    private final DebugVAO linesVAO = new DebugVAO(testPos, 2);
    private final DebugVAO pointsVAO = new DebugVAO(testPos, 2);

    @Getter
    enum DebugRenderMode {
        ModeScreenSpace(1),
        ModeWorldCamera(2);

        private final int value;

        DebugRenderMode(int value) {
            this.value = value;
        }

    }

    private final GLSLShaderProgram shader;

    /**
     * @param e Build RenderPipeline event
     */
    public DebugRenderStage(EngineBuildRenderPipelineEvent e) {
        super(e.moduleCore.getWindow());
        shader = (GLSLShaderProgram) AssetManager.getAssetS(new Identifier("spellbook", "shaders/SpellbookDebugShader.glsl"));

        // Uniforms
        shader.createUniform("projectionViewMatrix");
        shader.createUniform("color");
        shader.createUniform("mode");
    }

    @Override
    public String getName() {
        return "Debug";
    }

    @Override
    protected void draw(Rect r, FrameData frameData) {
        // Projection matrix
        float w = r.width;
        float h = r.height;
        float halfW = w * 0.5f;
        float halfH = h * 0.5f;

        Matrix4f projection = new Matrix4f().ortho(-halfW, halfW, halfH, -halfH, -1, 1);

        // Bind Debug Shader
        shader.bind();
        shader.setUniform("projectionViewMatrix", projection.mul(Camera.activeCamera.getViewMatrix()));

        // Render
        drawColliders(frameData);
        shader.setUniform("mode", DebugRenderMode.ModeWorldCamera.value);

        // Unbind
        shader.unbind();
    }

    private static final float CIRCLE_DETAIL = 32;

    private void drawCircle(Vector2f point, float radius) {
        GL11.glBegin(GL_LINE_LOOP);
        for (int i = 0; i < CIRCLE_DETAIL; i++) {
            float z = i / CIRCLE_DETAIL * 360;
            float x = radius * (float) Math.cos(Math.toRadians(z));
            float y = radius * (float) Math.sin(Math.toRadians(z));

            glVertex2f(x + point.x, y + point.y);
        }
        GL11.glEnd();
    }

    private void drawColliders(FrameData frameData) {
        HashSet<Rect> rects = new HashSet<>();
        HashSet<Vector2f> points = new HashSet<>();
        shader.setUniform("mode", DebugRenderMode.ModeWorldCamera.value);
        shader.setUniform("color", Color.yellow);

        for (Collider2D collider2D : frameData.newton2DSolids) {
            if (collider2D instanceof BoxCollider2D) rects.add(((BoxCollider2D) collider2D).getWorldPositionRect());
            else if (collider2D instanceof CircleCollider2D) {
                drawCircle(collider2D.getCenter(), ((CircleCollider2D) collider2D).radius);
            }

            points.add(collider2D.getCenter());
        }

        drawRectList(rects);
        shader.setUniform("color", Color.red);
        drawPointList(points);
    }

    private void drawRectList(Set<Rect> rectSet) {
        float[] vertices = new float[16 * rectSet.size()];
        int i = 0;
        for (Rect r : rectSet) {
            // C1 to C2
            vertices[i * 16] = r.x;
            vertices[i * 16 + 1] = r.y;
            vertices[i * 16 + 2] = r.x + r.width;
            vertices[i * 16 + 3] = r.y;

            // C2 to C3
            vertices[i * 16 + 4] = r.x + r.width;
            vertices[i * 16 + 5] = r.y;
            vertices[i * 16 + 6] = r.x + r.width;
            vertices[i * 16 + 7] = r.y - r.height;

            // C3 to C4
            vertices[i * 16 + 8] = r.x + r.width;
            vertices[i * 16 + 9] = r.y - r.height;
            vertices[i * 16 + 10] = r.x;
            vertices[i * 16 + 11] = r.y - r.height;

            // C4 to C1
            vertices[i * 16 + 12] = r.x;
            vertices[i * 16 + 13] = r.y - r.height;
            vertices[i * 16 + 14] = r.x;
            vertices[i * 16 + 15] = r.y;

            i++;
            // THE RECT
            // C1 C2
            // C4 C3
        }

        linesVAO.put(vertices);
        linesVAO.draw(GL_LINES);
    }

    private void drawPointList(Set<Vector2f> points) {
        float[] vertices = new float[2 * points.size()];
        int i = 0;
        for (Vector2f v : points) {
            vertices[i * 2] = v.x;
            vertices[i * 2 + 1] = v.y;
            i++;
        }

        pointsVAO.put(vertices);
        pointsVAO.draw(GL_POINTS);
    }

    @Override
    protected void destroy() {
        shader.unreference();
        linesVAO.destroy();
        pointsVAO.destroy();
    }
}
