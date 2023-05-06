package dk.sebsa.spellbook.phys.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.math.Vector2f;
import lombok.Getter;

/**
 * Abstract representation of a 2 dimensional collider component
 * @author sebs
 * @since 1.0.0
 */
public abstract class Collider2D implements Component {
    @Getter protected Entity entity;
    /**
     * Where the colliders is anchored to the entity
     * 0.5f 0.5f: the middle of the collider resides on the entities pos
     */
    public Vector2f anchor = new Vector2f(0.5f, 0.5f);

    /**
     * The center of the collider
     */
    @Getter private Vector2f center;

    @Override
    public void onEnable(Entity entity) {
        this.entity = entity;
    }

    private void calcCenter() {
        center.set(entity.transform.getGlobalPosition().x, entity.transform.getGlobalPosition().y);
        center = center.add(center.mul(anchor.sub(0.5f)));
    }

    @Override
    public void lateUpdate(FrameData frameData) {
        if(center == null) { center = new Vector2f(); calcCenter(); }
        if(entity.transform.isDirty())  {
            frameData.newton2DMovers.add(this);
            calcCenter();
        } else                            frameData.newton2DSolids.add(this);
    }

    /**
     * Check weather this collider collides with a collider
     * If collides handle collision stuff also xD
     *
     * @param collider The collider to check collision with
     */
    public abstract void collides(BoxCollider2D collider);

    /**
     * Check weather this collider collides with a collider
     * If collides handle collision stuff also xD
     *
     * @param collider The collider to check collision with
     */
    public abstract void collides(CircleCollider2D collider);
}
