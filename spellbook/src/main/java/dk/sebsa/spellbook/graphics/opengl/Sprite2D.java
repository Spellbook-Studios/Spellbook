package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.graphics.opengl.renderer.GLSpriteRenderer;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;
import lombok.CustomLog;

import java.util.Collection;
import java.util.Map;

/**
 * Renders 2d sprites
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class Sprite2D {
    private static Mesh2D mainMesh;
    private static GLSpriteRenderer renderer;

    /**
     * Inits the renderer
     *
     * @param e The engineLoadEvent
     */
    public static void init(EngineLoadEvent e) {
        if (mainMesh != null) return;

        mainMesh = Mesh2D.getRenderMesh();
        renderer = new GLSpriteRenderer(new Identifier("spellbook", "shaders/SpellbookSprite.glsl"));
    }

    /**
     * Renders a list of spriterenders to the screen
     *
     * @param window    The window to render to
     * @param r         Where to render to
     * @param frameData The spriterenders to render
     */
    public static void renderSprites(GLFWWindow window, Rect r, FrameData frameData) {
        renderer.begin(r);

        for (int i = 0; i < frameData.getRenderSprite().length; i++) {
            Map<Sprite, Collection<SpriteRenderer>> layer = frameData.getRenderSprite()[i];
            for (Sprite s : layer.keySet()) {
                renderer.setMaterial(s.getMaterial());

                for (SpriteRenderer sr : layer.get(s)) {
                    sr.draw(renderer);
                }
            }
        }

        renderer.end();
    }

    /**
     * Cleanup assets used by Sprite2D
     */
    public static void destroy() {
        renderer.destroy();
    }
}
