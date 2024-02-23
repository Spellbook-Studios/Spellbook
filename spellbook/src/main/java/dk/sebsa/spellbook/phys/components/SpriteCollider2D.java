package dk.sebsa.spellbook.phys.components;

import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;

/**
 * A boxcollider that follows the size and scale of a spriterenderer
 * @author sebs
 * @since 1.0.0
 */
public class SpriteCollider2D extends BoxCollider2D {
    /**
     * @param renderer The renderer to scale to
     */
    public SpriteCollider2D(SpriteRenderer renderer) {
        size.set(renderer.getSprite().getOffset().getSize().mul(renderer.scale));
    }
}
