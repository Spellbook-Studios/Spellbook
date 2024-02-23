package dk.sebsa.spellbook.debug;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.graphics.opengl.Material;
import dk.sebsa.spellbook.graphics.opengl.RenderStage;
import dk.sebsa.spellbook.graphics.opengl.renderer.GLRenderer;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;
import dk.sebsa.spellbook.phys.components.CircleCollider2D;
import dk.sebsa.spellbook.phys.components.Collider2D;
import lombok.Getter;

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
    private static final int CIRCLE_DETAIL = 32;
    private static final Material matYellow = new Material(Color.yellow);
    private static final Material matRed = new Material(Color.red);
    private final GLRenderer renderer;
    private final HashSet<Rect> rects = new HashSet<>();
    private final HashSet<Vector2f> points = new HashSet<>();
    private final HashSet<Circle> circles = new HashSet<>();


    /**
     * @param e Build RenderPipeline event
     */
    public DebugRenderStage(EngineBuildRenderPipelineEvent e) {
        super(e.moduleCore.getWindow());
        renderer = new GLRenderer(new Identifier("spellbook", "debug/shaders/SpellbookDebugShader.glsl"));
        renderer.getShader().createUniform("mode");
    }

    @Override
    public String getName() {
        return "Debug";
    }

    @Override
    protected void draw(Rect r, FrameData frameData) {
        renderer.begin(r);
        searchColliders(frameData);
        drawColliders();
        renderer.end();
    }

    public void searchColliders(FrameData frameData) {
        rects.clear(); points.clear(); circles.clear();
        for (Collider2D collider2D : frameData.newton2DSolids) {
            if (collider2D instanceof BoxCollider2D)
                rects.add(((BoxCollider2D) collider2D).getWorldPositionRect());
            if (collider2D instanceof CircleCollider2D)
                circles.add(new Circle(collider2D.getCenter(), ((CircleCollider2D) collider2D).radius));


            points.add(collider2D.getCenter());
        }
    }

    private void drawColliders() {
        renderer.getShader().setUniform("mode", DebugRenderMode.ModeWorldCamera.value);

        drawRectList(rects);
        drawCircleList(circles);
        drawPointList(points);
    }

    private void drawRectList(Set<Rect> rectSet) {
        renderer.setMaterial(matYellow);
        renderer.setMode(GL_LINES);

        for (Rect r : rectSet) {
            Vector2f c1 = new Vector2f(r.x, r.y),
                    c2 = new Vector2f(r.x + r.width, r.y),
                    c3 = new Vector2f(r.x + r.width, r.y - r.height),
                    c4 = new Vector2f(r.x, r.y -r.height);

            renderer.drawLine(c1, c2);
            renderer.drawLine(c2, c3);
            renderer.drawLine(c3, c4);
            renderer.drawLine(c4, c1);
        }
    }

    private void drawPointList(Set<Vector2f> points) {
        renderer.setMaterial(matRed);
        renderer.setMode(GL_POINTS);
        for (Vector2f v : points) {
            renderer.drawPoint(v);
        }
    }

    private void drawCircleList(Set<Circle> circles) {
        renderer.setMaterial(matYellow);
        renderer.setMode(GL_LINE_LOOP);
        for(Circle c : circles) {
            for (int i = 0; i < CIRCLE_DETAIL; i++) {
                float z = (float) i / CIRCLE_DETAIL * 360;
                float x = c.radius * (float) Math.cos(Math.toRadians(z));
                float y = c.radius * (float) Math.sin(Math.toRadians(z));

                renderer.drawPoint(c.pos.x + x, c.pos.y + y);
            }

            renderer.flush();
        }
    }

    @Override
    protected void destroy() {
        renderer.destroy();
    }

    @Getter
    enum DebugRenderMode {
        ModeScreenSpace(1),
        ModeWorldCamera(2);

        private final int value;

        DebugRenderMode(int value) {
            this.value = value;
        }

    }

    private record Circle(Vector2f pos, float radius) {}
}
