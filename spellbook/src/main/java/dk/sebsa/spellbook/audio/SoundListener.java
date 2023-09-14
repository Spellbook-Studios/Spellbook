package dk.sebsa.spellbook.audio;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.ecs.Component;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.math.Vector3f;

import static org.lwjgl.openal.AL10.*;

/**
 * Hears audio
 * @author sebs
 * @since 1.0.0
 */
public class SoundListener implements Component {
    /**
     * A soundlistener placed at 0,0,0
     */
    public SoundListener() {
        this(new Vector3f(0, 0, 0));
    }

    /**
     * @param position The current position of this soundlistener
     */
    public SoundListener(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    /**
     * Sets the speed of the sound listener
     * @param speed The speed in each cardinal direction
     */
    public void setSpeed(Vector3f speed) {
        alListener3f(AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    /**
     * Sets the current position of the sound listener
     * @param position The current position of the sound listener
     */
    public void setPosition(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    /**
     * Sets the orientation of this soundlistener
     * @param at Direction this is looking at
     * @param up Direction upwards from listener
     *           <a href="https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/chapter22/listener_at_up.png">...</a>
     */
    public void setOrientation(Vector3f at, Vector3f up) {
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        alListenerfv(AL_ORIENTATION, data);
    }

    // COMPONENT
    private Entity entity;

    @Override
    public void onEnable(Entity entity) {
        this.entity = entity;
        setPosition(entity.transform.getGlobalPosition());
        setSpeed(new Vector3f(0,0,0));
    }

    @Override
    public void update(FrameData frameData) {
        setPosition(entity.transform.getGlobalPosition());
    }
}
