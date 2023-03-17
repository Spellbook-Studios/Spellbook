package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.core.Application;

/**
 * Indicates to the engine it is time to setup classes
 * NO assets can be used yet and windows are not initilized yett.
 * Use this to do core loading and setup
 * @author sebs
 * @since 0.0.1
 */
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
