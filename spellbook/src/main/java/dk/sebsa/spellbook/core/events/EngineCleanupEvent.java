package dk.sebsa.spellbook.core.events;

/**
 * Tells the engine its time to cleanup
 * Destroy everything
 * Delete all heap and stack memory usages
 * @since 0.0.1
 * @author sebs
 */
public class EngineCleanupEvent extends Event {
    @Override
    protected EventType eventType() {
        return EventType.engineCleanup;
    }
}
