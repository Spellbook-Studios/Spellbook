package dk.sebsa.spellbook.core.events;

/**
 * Tells the engine its time to cleanup
 * Destroy everything
 * Delete all heap and stack memory usages
 * @since 1.0.0
 * @author sebs
 */
public class EngineCleanupEvent extends Event {
    @Override
    public EventType eventType() {
        return EventType.engineCleanup;
    }
}
