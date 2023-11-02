package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.threading.DynamicTaskGroup;
import lombok.RequiredArgsConstructor;

/**
 * Indicates to the engine it is time to setup classes
 * NO assets can be used yet and windows are not initilized yett.
 * Use this to do core loading and setup
 * It is recommended if possible to do the work in threads using the task group provided
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineInitEvent extends Event {
    /**
     * Spellbook capabilities
     */
    public final SpellbookCapabilities capabilities;
    /**
     * The main application
     */
    public final Application application;
    /**
     * Use this to schedule tasks to run, the event is only over when all tasks in this group is completed
     */
    public final DynamicTaskGroup tasks;

    @Override
    public EventType eventType() {
        return EventType.engineInit;
    }
}
