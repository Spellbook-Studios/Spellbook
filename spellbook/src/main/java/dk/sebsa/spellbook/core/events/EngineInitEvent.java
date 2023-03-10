package dk.sebsa.spellbook.core.events;

public class EngineInitEvent extends Event {
    @Override
    protected EventType eventType() {
        return EventType.engineInit;
    }
}
