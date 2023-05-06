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
    protected boolean matrixDirty = true;
    /**
     * Weather the transform has been modified within the last frame
     * Resets after Component.lateUpdate
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
    @Getter protected final Vector3f globalPosition = new Vector3f();
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
    public void setPosition(Vector3f pos) { this.localPosition.set(pos); matrixDirty = true; isDirty = true; recalculateGlobalTransformations(); }
    /**
     * Sets the localPosition of this entity, and recalculates it's global pos afterwards
     * @param x The x pos of the entity
     * @param y The y pos of the entity
     * @param z The z pos of the entity
     */
    public void setPosition(float x, float y, float z) { this.localPosition.set(x, y, x); matrixDirty = true; isDirty = true; recalculateGlobalTransformations(); }


    protected void recalculateLocalTransformation() {
        parent = entity.getParent().transform;
        matrixDirty = true; isDirty = true;
        localPosition.set(parent.globalPosition.x - globalPosition.x, parent.globalPosition.y - globalPosition.y, parent.globalPosition.z - globalPosition.z);

        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    protected void recalculateGlobalTransformations() {
        parent = entity.getParent().transform;
        matrixDirty = true; isDirty = true;
        globalPosition.set(parent.globalPosition.x + localPosition.x, parent.globalPosition.y + localPosition.y, parent.globalPosition.z + localPosition.z);

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
        if(matrixDirty) {
            matrixDirty = false;
            transformMatrix.setTransformation(pos2D.set(globalPosition.x, globalPosition.y), 0, Vector2f.VECTOR2F_ONE);
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

    /**
     * Moves the transform by an offset
     * @param v Offset position
     */
    public void move(Vector3f v) {
        setPosition(localPosition.add(v));
    }

    /**
     * Resets the dirty boolean
     */
    public void cleanDirt() {
        isDirty = false;
    }
}
