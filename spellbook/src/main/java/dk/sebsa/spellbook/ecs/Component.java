package dk.sebsa.spellbook.ecs;

import dk.sebsa.spellbook.FrameData;
import lombok.Getter;

/**
 * Represents a reusable functionality that can be applied to entities
 *
 * @author sebs
 * @since 1.0.0
 */
public abstract class Component {
    /**
     * Weather the component is enabled, i.e. the logic should be run
     */
    public boolean enabled = false;
    @Getter
    protected Entity entity;

    /**
     * Called when the component is attached to the entity
     * And when enabled after being disabled
     */
    public void onEnable() {

    }

    /**
     * Called once a frame during EventTypes.engineFrameProcess
     *
     * @param frameData Data from current frame. Contains input etc.
     */
    public void update(FrameData frameData) {
    }

    /**
     * Called once a frame during EventTypes.engineFrameProcess after component.update
     *
     * @param frameData Data from current frame. Contains input etc.
     */
    public void lateUpdate(FrameData frameData) {
    }

    /**
     * Called once a frame during EventTypes.engineRender
     */
    public void render() {
    }

    /**
     * Called once a component is removed from an entity
     * And when disabled
     */
    public void onDisable() {
    }

    /**
     * Creates a new enabled component
     * Components should only be instantiated when immedietly added to an entity
     *
     * @param e The entity to attach to
     */
    public void init(Entity e) {
        this.entity = e;
        if (!entity.components.contains(this)) entity.components.add(this);
        onEnable();

        enabled = true;
    }
}
