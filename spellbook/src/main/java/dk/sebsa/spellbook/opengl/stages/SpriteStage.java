package dk.sebsa.spellbook.opengl.stages;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.FBO;
import dk.sebsa.spellbook.opengl.RenderStage;
import dk.sebsa.spellbook.opengl.Sprite;
import dk.sebsa.spellbook.opengl.Sprite2D;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;

public class SpriteStage extends RenderStage {
    private final AssetReference spriteR;
    private Sprite sprite;
    private SpriteRenderer spriteRenderer;

    public SpriteStage(GLFWWindow window) {
        super(window);
        spriteR = AssetManager.getAssetS("/spellbook/32.spr");
        sprite = spriteR.get();
        spriteRenderer = new SpriteRenderer(sprite);
        spriteRenderer.scale = 10f;
    }

    @Override
    public String getName() {
        return "2D<Sprite>";
    }

    @Override
    protected void draw(FBO prevFBO, Rect r) {
        drawPreviousFBO(prevFBO);
        Sprite2D.renderSprite(window, r, spriteRenderer);
    }

    @Override
    protected void destroy() {
        sprite = null;
        spriteRenderer = null;
        spriteR.unRefrence();
    }
}
