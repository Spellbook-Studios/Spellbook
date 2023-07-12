package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;

/**
 * Everything is initilized and all resources are ready
 * The engine can now create and prepare stuff for the first frame
 * @author sebs
 * @since 1.0.0
 */
public class EngineFirstFrameEvent extends Event {
    /**
     * The ECS.ROOT entity
     */
    public final Entity root;

    /**
     * The spellbook application
     */
    public final Application application;

    /**
     * @param application The spellbook application
     */
    public EngineFirstFrameEvent(Application application) {
        this.root = ECS.ROOT;
        this.application = application;
    }

    @Override
    public EventType eventType() {
        return EventType.engineFirstFrame;
    }
}
