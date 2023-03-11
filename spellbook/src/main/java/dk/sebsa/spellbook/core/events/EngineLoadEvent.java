package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;

/**
 * Signifies to engine modules that they should load assets and prepare for the first frame
 * @author sebs
 * @since 0.0.1
 */
public class EngineLoadEvent extends Event {
    public final SpellbookCapabilities capabilities;

    public EngineLoadEvent(SpellbookCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    protected EventType eventType() {
        return EventType.engineLoad;
    }
}
