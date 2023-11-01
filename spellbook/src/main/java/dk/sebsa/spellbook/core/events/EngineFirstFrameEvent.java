package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;
import lombok.RequiredArgsConstructor;

/**
 * Everything is initilized and all resources are ready
 * The engine can now create and prepare stuff for the first frame
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineFirstFrameEvent extends Event {
    /**
     * The ECS.ROOT entity
     */
    public final Entity root = ECS.ROOT;

    /**
     * The spellbook application
     */
    public final Application application;

    @Override
    public EventType eventType() {
        return EventType.engineFirstFrame;
    }
}
