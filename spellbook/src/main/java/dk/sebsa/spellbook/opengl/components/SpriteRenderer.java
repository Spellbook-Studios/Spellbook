package dk.sebsa.spellbook.opengl.components;

import dk.sebsa.spellbook.math.Matrix4x4f;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.Sprite;
import lombok.Getter;

/**
 * Handles the rendering of a sprite
 * This is meant to represent a sprite to be rendered
 * @author sebs
 * @since 0.0.1
 */
public class SpriteRenderer {
    @Getter private final Sprite sprite;

    /**
     * @param sprite The sprite that this encapsulates
     */
    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
    }

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
     * Sets a shaders uniforms with the values of this SpriteRenderer
     * @param shader A shader that is prepared for sprite rendering by Sprite2D
     */
    public void setUniforms(GLSLShaderProgram shader) {
        shader.setUniform("objectScale", scale, scale);
        shader.setUniform("anchor", anchor);

        Matrix4x4f matrix = new Matrix4x4f();
        matrix.setTransformation(new Vector2f(0,0), 0, Vector2f.VECTOR2F_ONE);
        shader.setUniform("transformMatrix", matrix);

        // UV Stuff
        Rect uvRect = sprite.getUV();
        shader.setUniform("offset", uvRect.x, uvRect.y, uvRect.width, uvRect.height);
        shader.setUniform("pixelScale", sprite.getOffset().width, sprite.getOffset().height);
    }
}
