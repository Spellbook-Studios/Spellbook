package dk.sebsa.spellbook.phys.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.math.Vector3f;
import lombok.Getter;

/**
 * A 2 dimensional box shaped collider
 * @author sebs
 * @since 1.0.0
 */
public class BoxCollider2D extends Collider2D {
    /**
     * The size of the collider
     */
    public Vector2f size = new Vector2f(100, 100);
    @Getter private Rect worldPositionRect;

    @Override
    public void lateUpdate(FrameData frameData) {
        super.lateUpdate(frameData);
        if(worldPositionRect == null) {
            worldPositionRect = new Rect();
            calculateWorldPosRect();
        } else if(entity.transform.isDirty()) calculateWorldPosRect();
    }

    private void calculateWorldPosRect() {
        float halfW = size.x * anchor.x;
        float halfH = size.y * anchor.y;
        worldPositionRect.set(entity.transform.getGlobalPosition().x - halfW, entity.transform.getGlobalPosition().y + halfH, size.x, size.y);
    }

    private static final Rect intersect = new Rect(), colliderRect = new Rect();

    @Override
    public void collides(BoxCollider2D collider) {
        colliderRect.set(collider.getWorldPositionRect());

        if(getWorldPositionRect().intersects(colliderRect)) {
            worldPositionRect.getIntersection(colliderRect, intersect);
            System.out.println(intersect);
            // Pushes the collider out
            if (intersect.width < intersect.height) {
                if (colliderRect.x < intersect.x) collider.getEntity().transform.move(new Vector3f(-intersect.width, 0, 0));
                else collider.getEntity().transform.move(new Vector3f(intersect.width, 0, 0));
            } else {
                if (colliderRect.y < intersect.y) collider.getEntity().transform.move(new Vector3f(0, -intersect.height, 0));
                else collider.getEntity().transform.move(new Vector3f(0, intersect.height, 0));
            }
        }
    }
}
