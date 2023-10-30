package dk.sebsa.spellbook.ecs;

import dk.sebsa.spellbook.math.Vector3f;

/**
 * A transform for an entity which is always at (0,0,0)
 * Made for use in Scene entities
 * Cannot be moved
 *
 * @author sebs
 * @since 1.0.0
 */
public class LockedTransform extends Transform {
    /**
     * @param entity The entity this is linked to
     */
    protected LockedTransform(Entity entity) {
        super(entity);
    }

    @Override
    public void setPosition(Vector3f pos) {
    }

    @Override
    public void setPosition(float x, float y, float z) {
    }


    @Override
    protected void recalculateLocalTransformation() {
        for (int i = 0; i < entity.getChildren().size(); i++)
            entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    @Override
    protected void recalculateGlobalTransformations() {
        for (int i = 0; i < entity.getChildren().size(); i++)
            entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }
}
