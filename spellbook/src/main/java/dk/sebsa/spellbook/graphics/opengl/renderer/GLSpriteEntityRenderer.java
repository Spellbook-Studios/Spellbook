package dk.sebsa.spellbook.graphics.opengl.renderer;

import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;

/**
 * Renders 2D objects in batches
 * (Same as GL2DRender just with model matricies)
 *
 * @author sebs
 * @since 1.0.0
 */
public class GLSpriteEntityRenderer extends GLSpriteRenderer {
    public GLSpriteEntityRenderer(Identifier shaderI) {
        super(shaderI);
        shader.createUniform("mModel");
    }

    public void drawSprite(SpriteRenderer sr) {
        Rect uvRect = sr.getSprite().getUV();

        Vector2f size = new Vector2f(sr.getSprite().getOffset().width, sr.getSprite().getOffset().height);

        drawQuad(new Rect(size.mul(sr.anchor).mul(-sr.scale), size.mul(sr.scale)),
                uvRect);
        shader.setUniform("mModel", sr.getEntity().transform.getMatrix2D());
        flush();
    }
}
