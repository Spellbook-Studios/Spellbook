package dk.sebsa.spellbook.opengl.stages;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.FBO;
import dk.sebsa.spellbook.opengl.RenderStage;
import dk.sebsa.spellbook.opengl.Sprite2D;

/**
 * A stage that renders all SpriteRenderer components
 * @since 1.0.0
 * @author sebs
 */
public class SpriteStage extends RenderStage {
    private final Rect renderResolution;

    /**
     * @param e Load event, containing capabilities and window
     */
    public SpriteStage(EngineLoadEvent e) {
        super(e.moduleCore.getWindow());
        renderResolution = e.capabilities.renderResolution;
    }

    @Override
    public String getName() {
        return "2D<Sprite>";
    }

    @Override
    protected void draw(FBO prevFBO, Rect r, FrameData frameData) {
        drawPreviousFBO(prevFBO);
        if(Camera.activeCamera == null) return;
        Sprite2D.renderSprites(window, renderResolution, frameData);
    }

    @Override
    protected void destroy() {

    }
}
