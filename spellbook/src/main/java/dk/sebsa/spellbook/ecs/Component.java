package dk.sebsa.spellbook.ecs;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a reusable functionality that can be applied to entities
 * @author sebsn
 * @since 1.0.0
 */
public abstract class Component {
    @Getter @Setter
    private boolean enabled = true;

    /**
     * The entity that holds this component
     * Should not be changed at all
     */
    protected Entity entity;

    /**
     * Called when the component is attached to the entity
     * And when enabled after being disabled
     */
    protected abstract void onEnable();

    /**
     * Called once a frame during EventTypes.engineFrameProcess
     */
    protected abstract void update();

    /**
     * Called once a frame during EventTypes.engineRender
     */
    protected abstract void render();

    /**
     * Called once a component is removed from an entity
     * And when disabled
     */
    protected abstract void onDisable();

    /**
     * Creates a new enabled component
     * Components should only be instantiated when immedietly added to an entity
     */
    void init(Entity e) {
        this.entity = e;
        if(!entity.components.contains(this)) entity.components.add(this);
        onEnable();
    }
}
