package dk.sebsa.components;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.math.Vector3f;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.phys.components.Collider2D;
import dk.sebsa.spellbook.phys.components.SpriteCollider2D;
import org.lwjgl.glfw.GLFW;

public class TappyController extends SpriteCollider2D {
    private static final float GRAVITY_SPEED = 400f;
    private static final float JUMP_ENERGY = 0.8f;
    private static final float VELOCITY_DROPFF = 5f;
    private float velocity = 1;

    private Entity e;

    public TappyController(SpriteRenderer renderer) {
        super(renderer);
    }

    @Override
    public void onEnable(Entity entity) {
        super.onEnable(entity);
        this.e = entity;
    }

    @Override
    public void onCollide(Collider2D other) {
        velocity = 0;
    }

    @Override
    public void update(FrameData frameData) {
        if (frameData.input.isKeyPressed(GLFW.GLFW_KEY_SPACE) || frameData.input.gamePads[0].buttonX)
            velocity = JUMP_ENERGY;
        else if (velocity <= 0)
            velocity = Math.max(velocity - ((VELOCITY_DROPFF * (-velocity + 0.1f)) * Time.getDeltaTime())
                    , -1);
        else
            velocity = Math.min(velocity - ((VELOCITY_DROPFF * (velocity + 0.1f)) * Time.getDeltaTime())
                    , 1.5f);

        e.transform.move(new Vector3f(0, GRAVITY_SPEED * velocity * Time.getDeltaTime(), 0));
        e.transform.setRotation(velocity * -30, 0, 0);
    }
}
