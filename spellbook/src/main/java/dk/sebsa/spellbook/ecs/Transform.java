package dk.sebsa.spellbook.ecs;

import dk.sebsa.spellbook.math.Matrix4x4f;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.math.Vector3f;
import lombok.Getter;

/**
 * Represents the absouloute and relative position and rotation of an entity
 * @author sebs
 * @since 1.0.0
 */
public class Transform {
    /**
     * Weather the transform has been modified within the last frame
     */
    @Getter protected boolean isDirty = true;
    /**
     * The entity that this transform belongs to
     */
    protected final Entity entity;

    private final Matrix4x4f transformMatrix = new Matrix4x4f();
    private Transform parent;

    /**
     * @param entity The entity this is linked to
     */
    protected Transform(Entity entity) {
        this.entity = entity;
    }


    /**
     * The global position of the entity
     * Equal to the localPosition of this, and all it's parents localPosition
     */
    @Getter protected final Vector3f position = new Vector3f();
    /**
     * The position of the entity under it's parent
     */
    @Getter protected final Vector3f localPosition = new Vector3f();

    /**
     * The rotation of the object
     * x roll
     * y pitch
     * z yaw
     */
    @Getter protected final Vector3f rotation = new Vector3f();

    // Setters
    /**
     * Sets the localPosition of this entity, and recalculates it's global pos afterwards
     * @param pos The new local position of the entity
     */
    public void setPosition(Vector3f pos) { this.localPosition.set(pos); isDirty = true; recalculateGlobalTransformations(); }
    /**
     * Sets the localPosition of this entity, and recalculates it's global pos afterwards
     * @param x The x pos of the entity
     * @param y The y pos of the entity
     * @param z The z pos of the entity
     */
    public void setPosition(float x, float y, float z) { this.localPosition.set(x, y, x); isDirty = true; recalculateGlobalTransformations(); }


    protected void recalculateLocalTransformation() {
        parent = entity.getParent().transform;
        isDirty = true;
        localPosition.set(parent.position.x - position.x, parent.position.y - position.y, parent.position.z - position.z);

        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    protected void recalculateGlobalTransformations() {
        parent = entity.getParent().transform;
        isDirty = true;
        position.set(parent.position.x + localPosition.x, parent.position.y + localPosition.y, parent.position.z + localPosition.z);

        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    private final Vector2f pos2D = new Vector2f();

    /**
     * @return The transform matrix of the entity
     */
    public Matrix4x4f getMatrix2D() {
        clean();
        return transformMatrix;
    }

    /**
     * If the entity has moved, it recalculates it's transform matrix
     */
    public void clean() {
        if(isDirty) {
            isDirty = false;
            transformMatrix.setTransformation(pos2D.set(position.x, -position.y), 0, Vector2f.VECTOR2F_ONE);
        }
    }

    /**
     * Rotates by an offset
     * @param offsetX Offset X
     * @param offsetY Offset Y
     * @param offsetZ Offset Z
     */
    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
}
