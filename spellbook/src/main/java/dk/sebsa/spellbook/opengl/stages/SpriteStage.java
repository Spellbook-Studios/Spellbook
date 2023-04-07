package dk.sebsa.spellbook.opengl.stages;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.FBO;
import dk.sebsa.spellbook.opengl.RenderStage;
import dk.sebsa.spellbook.opengl.Sprite;
import dk.sebsa.spellbook.opengl.Sprite2D;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;

/**
 * A stage that renders all SpriteRenderer components
 * @since 0.0.1
 * @author sebs
 */
public class SpriteStage extends RenderStage {
    private final AssetReference spriteR;
    private Sprite sprite;
    private SpriteRenderer spriteRenderer;
    private final Rect renderResolution;

    /**
     * @param e Load event, containing capabilities and window
     */
    public SpriteStage(EngineLoadEvent e) {
        super(e.moduleCore.getWindow());
        spriteR = AssetManager.getAssetS("/spellbook/32.spr");
        sprite = spriteR.get();
        spriteRenderer = new SpriteRenderer(sprite);
        spriteRenderer.scale = 1f;
        renderResolution = e.capabilities.renderResolution;
    }

    @Override
    public String getName() {
        return "2D<Sprite>";
    }

    @Override
    protected void draw(FBO prevFBO, Rect r) {
        drawPreviousFBO(prevFBO);
        Sprite2D.renderSprite(window, renderResolution, spriteRenderer);
    }

    @Override
    protected void destroy() {
        sprite = null;
        spriteRenderer = null;
        spriteR.unRefrence();
    }
}
