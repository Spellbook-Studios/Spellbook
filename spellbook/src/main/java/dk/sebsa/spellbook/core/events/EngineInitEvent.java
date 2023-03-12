package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.core.Application;

public class EngineInitEvent extends Event {
    public final Logger logger;
    public final SpellbookCapabilities capabilities;
    public final Application application;

    public EngineInitEvent(Logger logger, SpellbookCapabilities capabilities, Application application) {
        this.logger = logger;
        this.capabilities = capabilities;
        this.application = application;
    }

    @Override
    protected EventType eventType() {
        return EventType.engineInit;
    }
}
