package dk.sebsa.spellbook.opengl.components;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.math.Matrix4x4f;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.Sprite;

/**
 * Handles the rendering of a sprite
 * This is meant to represent a sprite to be rendered
 * @author sebs
 * @since 0.0.1
 */
public class SpriteRenderer extends Component {
    /**
     * The sprite that this spriterender renders
     */
    public Sprite sprite;

    /**
     * An AssetReference referencing a sprite
     */
    public AssetReference spriteR;

    /**
     * Where to place the sprite upon the entity
     * (0.5f, 0.5f) places the middle of the sprite directly on the position
     * (0f, 0f) places the top-left corner of the sprite on the position
     */
    public Vector2f anchor = new Vector2f(0.5f, 0.5f);
    /**
     * PENDING!!!!!!!!!!!
     */
    public float scale = 1.0f;

    /**
     * A spriterenderer without a sprite
     * Set the sprite with this.sprite = ??
     */
    public SpriteRenderer() {

    }

    /**
     * A spriterender that automatically references and unreferences sprites from an assetreference
     * @param spriteR Reference to a sprite
     */
    public SpriteRenderer(AssetReference spriteR) {
        this.spriteR = spriteR;
    }

    /**
     * Sets a shaders uniforms with the values of this SpriteRenderer
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
    protected void onEnable() {
        if(spriteR != null) sprite = spriteR.get();
    }

    @Override
    protected void update() {

    }

    @Override
    protected void render() {
        Spellbook.FRAME_DATA.addRenderSprite(this);
    }

    @Override
    protected void onDisable() {
        if(spriteR != null) { sprite = null; spriteR.unRefrence(); }
    }
}
