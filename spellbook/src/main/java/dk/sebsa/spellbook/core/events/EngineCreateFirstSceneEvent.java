package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.ecs.Entity;
import lombok.RequiredArgsConstructor;

/**
 * The engine is now ready to build the first scene
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineCreateFirstSceneEvent extends Event {
    /**
     * The root entity representing the scene
     */
    public final Entity ROOT;

    @Override
    public EventType eventType() {
        return EventType.engineCreateFirstScene;
    }
}
