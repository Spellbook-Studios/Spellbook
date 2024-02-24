package dk.sebsa.spellbook.phys.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.math.Mathf;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.math.Vector3f;
import lombok.Getter;

/**
 * A 2 dimensional box shaped collider
 *
 * @author sebs
 * @since 1.0.0
 */
public class BoxCollider2D extends Collider2D {
    private static final Rect intersect = new Rect(), colliderRect = new Rect();
    /**
     * The size of the collider
     */
    public final Vector2f size = new Vector2f(100, 100);
    @Getter
    private Rect worldPositionRect;
    private float innerCircle;
    private float outerCircle;

    @Override
    public void lateUpdate(FrameData frameData) {
        super.lateUpdate(frameData);
        if (worldPositionRect == null) {
            worldPositionRect = new Rect();
            calculateWorldPosRect();
        } else if (entity.transform.isDirty()) calculateWorldPosRect();
    }

    private void calculateWorldPosRect() {
        float halfW = size.x * anchor.x;
        float halfH = size.y * anchor.y;
        worldPositionRect.set(entity.transform.getGlobalPosition().x - halfW, entity.transform.getGlobalPosition().y + halfH, size.x, size.y);

        // SHOULD BE MOVED ...
        innerCircle = size.min() / 2.0f;
        outerCircle = sqr(sq(size.x) + sq(size.y));
    }

    @Override
    public void collides(BoxCollider2D collider) {
        colliderRect.set(collider.getWorldPositionRect());

        if (getWorldPositionRect().getOverlap(colliderRect, intersect)) {
            // Pushes the collider out
            if (!isTrigger && !collider.isTrigger) {
                if (intersect.width < intersect.height) {
                    if (colliderRect.x < intersect.x)
                        collider.getEntity().transform.move(new Vector3f(-intersect.width, 0, 0));
                    else collider.getEntity().transform.move(new Vector3f(intersect.width, 0, 0));
                } else {
                    if (colliderRect.y > intersect.y)
                        collider.getEntity().transform.move(new Vector3f(0, intersect.height, 0));
                    else collider.getEntity().transform.move(new Vector3f(0, -intersect.height, 0));
                }
            }
            onCollide(collider);
            collider.onCollide(this);
        }
    }

    private float sq(float i) {
        return i * i;
    }

    private float sqr(float i) {
        return (float) Math.sqrt(i);
    }

    @Override
    public void collides(CircleCollider2D collider) {
        float distanceBetweenCenters = Mathf.sqDistance(getCenter(), collider.getCenter());

        // The circle is definetly outside if it is not within the outer circle of this rectangele
        if (distanceBetweenCenters > sq(outerCircle + collider.radius)) {
            return;
        }

        // The cirtcle is definely inside if the circle is inside the inner circle
        if (distanceBetweenCenters < sq(innerCircle + collider.radius)) {
            moveCircleOut(collider);
            return;
        }

        // In mos cases this doesn't happen, because most cases are caught by the other code
        System.out.println("CHECK");
        Vector2f normalized = (collider.getCenter().sub(getCenter())).normalize();
        Vector2f outerPoint = collider.getCenter().sub(normalized.mul(collider.radius));
        if (worldPositionRect.inRect(outerPoint)) {
            moveCircleOut(collider);
        }
    }

    private void moveCircleOut(CircleCollider2D collider) {
        onCollide(collider);
        collider.onCollide(this);
        System.out.println("COLLIDE");
    }
}
