package dk.sebsa.spellbook.core.events;

public class EngineCleanupEvent extends Event {
    @Override
    protected EventType eventType() {
        return EventType.engineCleanup;
    }
}
