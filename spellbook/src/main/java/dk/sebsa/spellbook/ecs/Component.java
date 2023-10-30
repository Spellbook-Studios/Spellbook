package dk.sebsa.spellbook.ecs;

import dk.sebsa.spellbook.FrameData;

/**
 * Represents a reusable functionality that can be applied to entities
 *
 * @author sebs
 * @since 1.0.0
 */
public interface Component {
    /**
     * Weather the component is enabled, i.e. the logic should be run
     */
    boolean enabled = true;

    /**
     * Called when the component is attached to the entity
     * And when enabled after being disabled
     *
     * @param entity Entity, should be stored by the component
     */
    void onEnable(Entity entity);

    /**
     * Called once a frame during EventTypes.engineFrameProcess
     *
     * @param frameData Data from current frame. Contains input etc.
     */
    default void update(FrameData frameData) {
    }

    /**
     * Called once a frame during EventTypes.engineFrameProcess after component.update
     *
     * @param frameData Data from current frame. Contains input etc.
     */
    default void lateUpdate(FrameData frameData) {
    }

    /**
     * Called once a frame during EventTypes.engineRender
     */
    default void render() {
    }

    /**
     * Called once a component is removed from an entity
     * And when disabled
     */
    default void onDisable() {
    }

    /**
     * Creates a new enabled component
     * Components should only be instantiated when immedietly added to an entity
     *
     * @param e The entity to attach to
     */
    default void init(Entity e) {
        if (!e.components.contains(this)) e.components.add(this);
        onEnable(e);
    }
}
