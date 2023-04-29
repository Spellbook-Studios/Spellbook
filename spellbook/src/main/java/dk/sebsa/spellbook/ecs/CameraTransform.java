package dk.sebsa.spellbook.ecs;

import dk.sebsa.spellbook.math.Matrix4x4f;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A transform for use by cameras
 * @since 1.0.0
 * @author sebs
 */
public class CameraTransform extends Transform {
    @Getter private final Matrix4f viewMatrix = new Matrix4f();

    /**
     * @param entity The entity this is linked to
     */
    protected CameraTransform(Entity entity) {
        super(entity);
    }

    /**
     * Moves the position by an offset
     * @param offsetX Offset X
     * @param offsetY Offset Y
     * @param offsetZ Offset Z
     */
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            localPosition.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            localPosition.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            localPosition.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            localPosition.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        localPosition.y += offsetY;
        isDirty = true;
    }

    @Override
    public void clean() {
        if(isDirty) {
            viewMatrix.identity()
                    .translate(-position.x, position.y, -position.z);
        }

        super.clean();
    }
}
