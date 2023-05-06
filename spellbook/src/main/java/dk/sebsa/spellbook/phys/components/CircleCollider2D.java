package dk.sebsa.spellbook.phys.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import lombok.Getter;

/**
 * A collider in the shape of a circle
 * @author sebs
 * @since 1.0.0
 */
public class CircleCollider2D extends Collider2D {
    /**
     * Radius in worldspace
     */
    public float radius = 16;

    @Override
    public void collides(BoxCollider2D collider) {

    }

    @Override
    public void collides(CircleCollider2D collider) {

    }
}
