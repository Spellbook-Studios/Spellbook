package dk.sebsa.spellbook.opengl.components;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.opengl.GL2D;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.Sprite;
import lombok.CustomLog;

/**
 * Handles the rendering of a sprite
 * This is meant to represent a sprite to be rendered
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class SpriteRenderer implements Component {
    private Entity entity;

    /**
     * The sprite that this spriterender renders
     */
    public Sprite sprite;

    /**
     * The name of the sprite
     */
    public String spriteName;

    /**
     * Where to place the sprite upon the entity
     * (0.5f, 0.5f) places the middle of the sprite directly on the position
     * (0f, 0f) places the top-left corner of the sprite on the position
     */
    public final Vector2f anchor = new Vector2f(0.5f, 0.5f);
    /**
     * PENDING!!!!!!!!!!!
     */
    public float scale = 1.0f;

    /**
     * The layer to render to
     * Higher is render later, and therefore appear to be on top
     * Layer must be between 0 and SpellbookCapabilities.maxSpriteLayers-1;
     */
    public int layer = 0;

    /**
     * A spriterenderer without a sprite
     * Set the sprite with this.sprite = ??
     */
    public SpriteRenderer() {

    }

    /***
     * @param spriteName Name of the sprite
     */
    public SpriteRenderer(String spriteName) {
        this.spriteName = spriteName;
    }

    /**
     * Sets a shaders uniforms with the values of this SpriteRenderer
     *
     * @param shader A shader that is prepared for sprite rendering by Sprite2D
     */
    public void setUniforms(GLSLShaderProgram shader) {
        shader.setUniform("objectScale", scale, scale);
        shader.setUniform("anchor", anchor);

        shader.setUniform("transformMatrix", entity.transform.getMatrix2D());

        // UV Stuff
        Rect uvRect = sprite.getUV();
        shader.setUniform("offset", uvRect.x, uvRect.y, uvRect.width, uvRect.height);
        shader.setUniform("pixelScale", sprite.getOffset().width, sprite.getOffset().height);
    }

    @Override
    public void onEnable(Entity e) {
        this.entity = e;
        this.sprite = (Sprite) AssetManager.getAssetS(spriteName);
        if (sprite == null) {
            sprite = GL2D.missingSprite;
            logger.warn("SpriteRender sprite reference is null");
        }
    }

    @Override
    public void render() {
        Spellbook.FRAME_DATA.addRenderSprite(this);
    }

    @Override
    public void onDisable() {
        if (sprite != null && sprite != GL2D.missingSprite) sprite.unreference();
    }
}
