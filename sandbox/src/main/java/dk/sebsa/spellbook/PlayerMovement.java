package dk.sebsa.spellbook;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.io.GLFWInput;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.math.Vector2f;
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
    public final float speed = 30f;

    @Override
    public void update(FrameData frameData) {
        GLFWInput input = frameData.input;
        deltaMovement.zero();

        if(input.isKeyDown(GLFW.GLFW_KEY_W)) {
            deltaMovement.y += speed * 10;
        } if(input.isKeyDown(GLFW.GLFW_KEY_D)) {
            deltaMovement.x += speed * 10;
        } if(input.isKeyDown(GLFW.GLFW_KEY_S)) {
            deltaMovement.y -= speed * 10;
        } if(input.isKeyDown(GLFW.GLFW_KEY_A)) {
            deltaMovement.x -= speed * 10;
        }
        if(!deltaMovement.isZero()) entity.transform.setPosition(entity.transform.getLocalPosition().add(deltaMovement.mul(Time.getDeltaTime())));
    }
}
