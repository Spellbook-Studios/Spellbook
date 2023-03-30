package dk.sebsa.spellbook.opengl.components;

import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.Sprite;
import lombok.Getter;

public class SpriteRenderer {
    @Getter private final Sprite sprite;

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
    }

    public Vector2f anchor = new Vector2f(0.5f, 0.5f);
    public float scale = 1.0f;

    public void setUniforms(GLSLShaderProgram shader) {
        shader.setUniform("objectScale", scale, scale);
        shader.setUniform("anchor", anchor);

        // UV Stuff
        Rect uvRect = sprite.getUV();
        shader.setUniform("offset", uvRect.x, uvRect.y, uvRect.width, uvRect.height);
        shader.setUniform("pixelScale", sprite.getOffset().width, sprite.getOffset().height);
    }
}
