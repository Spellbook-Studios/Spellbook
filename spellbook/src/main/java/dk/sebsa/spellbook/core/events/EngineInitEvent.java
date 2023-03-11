package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.mana.Logger;

public class EngineInitEvent extends Event {
    public final Logger logger;
    public final SpellbookCapabilities capabilities;

    public EngineInitEvent(Logger logger, SpellbookCapabilities capabilities) {
        this.logger = logger;
        this.capabilities = capabilities;
    }

    @Override
    protected EventType eventType() {
        return EventType.engineInit;
    }
}
