package dk.sebsa.spellbook.graphics.opengl.components;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.graphics.opengl.GL2D;
import dk.sebsa.spellbook.graphics.opengl.Sprite;
import dk.sebsa.spellbook.graphics.opengl.SpriteSheet;
import dk.sebsa.spellbook.graphics.opengl.renderer.GLSpriteEntityRenderer;
import dk.sebsa.spellbook.math.Vector2f;
import lombok.CustomLog;
import lombok.Getter;

/**
 * Handles the rendering of a sprite
 * This is meant to represent a sprite to be rendered
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class SpriteRenderer extends Component {
    /**
     * Where to place the sprite upon the entity
     * (0.5f, 0.5f) places the middle of the sprite directly on the position
     * (0f, 0f) places the top-left corner of the sprite on the position
     */
    public final Vector2f anchor = new Vector2f(0.5f, 0.5f);
    /**
     * The identifier of the sprite
     */
    public Identifier identifier = new Identifier("spellbook", "missing.spr");
    /**
     * PENDING!!!!!!!!!!!
     */
    public float scale = 1.0f;
    /**
     * The layer to render to
     * Higher is render later, and therefore appear to be on top
     * Layer must be between 0 and SpellbookCapabilities.maxSpriteLayers-1;
     */
    public int layer = Spellbook.instance.getCapabilities().spriteMaxLayer > 1 ? 1 : 0;
    @Getter
    private Sprite sprite;
    private SpriteSheet spriteSheet;
    private String spriteSheetSprite;

    /**
     * SpriteRenderer with the default sprite
     */
    public SpriteRenderer() {
    }

    /**
     * @param identifier Identifier of a sprite
     */
    public SpriteRenderer(Identifier identifier) {
        this.identifier = identifier;
    }

    /**
     * @param spritesheet Identifier of a spritesheet
     * @param sprite      The name of the sprite
     */
    public SpriteRenderer(Identifier spritesheet, String sprite) {
        this.identifier = spritesheet;
        this.spriteSheetSprite = sprite;
    }

    /**
     * Renders itself using a BatchRenderer
     *
     * @param renderer Batchrenderer to render with
     */
    public void draw(GLSpriteEntityRenderer renderer) {
        if (sprite == null) onEnable();

        renderer.drawSprite(this);
    }

    public void onEnable() {
        if (sprite != null) return;
        if (spriteSheetSprite != null) { // If this sprite is set using a spritesheet
            this.spriteSheet = (SpriteSheet) AssetManager.getAssetS(identifier);
            this.sprite = spriteSheet.spr(spriteSheetSprite);
        } else // If it is just a sprite
            this.sprite = (Sprite) AssetManager.getAssetS(identifier);

        if (sprite == null) {
            sprite = GL2D.missingSprite;
            logger.warn("SpriteRender sprite reference is null");
        }
    }

    @Override
    public void render() {
        if (sprite != null)
            Spellbook.FRAME_DATA.addRenderSprite(this);
    }

    @Override
    public void onDisable() {
        if (spriteSheetSprite != null) {
            spriteSheet.unreference();
            sprite = null;
            spriteSheet = null;
        } else if (sprite != null && sprite != GL2D.missingSprite) {
            sprite.unreference();
            sprite = null;
        }
    }
}
