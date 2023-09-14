package dk.sebsa.spellbook.ecs;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains utilities for matrix math and movement of a camera entity
 *
 * @author sebs
 * @since 1.0.0
 */
public class Camera extends Entity {
    /**
     * List of all currently active cameras
     */
    public static final List<Camera> CAMERAS = new ArrayList<>();

    /**
     * Currently active camera
     */
    public static Camera activeCamera;

    /**
     * @param parent Parent entity or scene entity
     */
    public Camera(Entity parent) {
        super(parent, "SPELLBOOK-CAM");
        CAMERAS.add(this);
        if(activeCamera==null) activeCamera = this;
    }

    /**
     * @return This camera's view matrix
     */
    public Matrix4f getViewMatrix() {
        this.transform.clean();
        return ((CameraTransform) this.transform).getViewMatrix();
    }
}
