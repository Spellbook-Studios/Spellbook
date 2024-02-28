package dk.sebsa;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.io.GLFWInput;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.math.Vector3f;
import org.lwjgl.glfw.GLFW;

public class PlayerMovement extends Component {
    public final float speed = 200f;
    private final Vector3f deltaMovement = new Vector3f();

    @Override
    public void update(FrameData frameData) {
        GLFWInput input = frameData.input;
        deltaMovement.zero();

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
        entity.transform.move(deltaMovement.mul(speed).mul(Time.getDeltaTime()));
    }
}
