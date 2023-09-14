package dk.sebsa.spellbook.phys.components;

import dk.sebsa.spellbook.math.*;

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

    private final Vector2f circle1 = new Vector2f();
    private final Vector2f circle2 = new Vector2f();

    @Override
    public void collides(CircleCollider2D collider) {
        circle1.set(getCenter());
        circle2.set(collider.getCenter());

        float distanceSq = Math.abs(Mathf.sqDistance(circle1, circle2));
        if(distanceSq < (radius + collider.radius) * (radius + collider.radius)) {
            float x = (circle1.x + circle2.x) / 2;
            float y = (circle1.y + circle2.y) / 2;
            float dist = (float) Math.sqrt(distanceSq);

            circle1.x = x + radius * (circle1.x - circle2.x) / dist;
            circle1.y = y + radius * (circle1.y - circle2.y) / dist;
            circle2.x = x + collider.radius * (circle2.x - circle1.x) / dist;
            circle2.y = y + collider.radius * (circle2.y - circle1.y) / dist;

            collider.entity.transform.setPosition(circle2.x, circle2.y, 0);
        }
    }
}
