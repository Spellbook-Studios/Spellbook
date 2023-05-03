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

    @Override
    public void onEnable(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void lateUpdate(FrameData frameData) {
        if(entity.transform.isDirty())   frameData.newton2DMovers.add(this);
        else                        frameData.newton2DSolids.add(this);
    }

    /**
     * Check weather this collider collides with a collider
     * If collides handle collision stuff also xD
     *
     * @param collider The collider to check collision with
     */
    public abstract void collides(BoxCollider2D collider);
}
