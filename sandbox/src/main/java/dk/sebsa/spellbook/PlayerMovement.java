package dk.sebsa.spellbook;

import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.io.GLFWInput;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.math.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * @author sebs
 */
public class PlayerMovement implements Component {
    private Entity entity;

    @Override
    public void onEnable(Entity e) {
        this.entity = e;
    }

    private final Vector3f deltaMovement = new Vector3f();
    public final float speed = 300f;

    @Override
    public void update(FrameData frameData) {
        GLFWInput input = frameData.input;
        deltaMovement.zero();

        if (Sandbox.gamePad != null) {
            deltaMovement.set(Sandbox.gamePad.axisLeft.x, -Sandbox.gamePad.axisLeft.y, 0);
        }

        if (input.isKeyDown(GLFW.GLFW_KEY_W)) {
            deltaMovement.y = 1;
        }
        if (input.isKeyDown(GLFW.GLFW_KEY_D)) {
            deltaMovement.x = 1;
        }
        if (input.isKeyDown(GLFW.GLFW_KEY_S)) {
            deltaMovement.y = -1;
        }
        if (input.isKeyDown(GLFW.GLFW_KEY_A)) {
            deltaMovement.x = -1;
        }
        if (!deltaMovement.isZero()) entity.transform.move(deltaMovement.mul(speed).mul(Time.getDeltaTime()));
    }
}
