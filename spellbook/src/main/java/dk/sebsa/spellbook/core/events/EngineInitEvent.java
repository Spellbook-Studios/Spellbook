package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.SpellbookLogger;
import lombok.RequiredArgsConstructor;

/**
 * Indicates to the engine it is time to setup classes
 * NO assets can be used yet and windows are not initilized yett.
 * Use this to do core loading and setup
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineInitEvent extends Event {
    /**
     * The main spellbook logger
     */
    public final SpellbookLogger logger;
    /**
     * Spellbook capabilities
     */
    public final SpellbookCapabilities capabilities;
    /**
     * The main application
     */
    public final Application application;

    @Override
    public EventType eventType() {
        return EventType.engineInit;
    }
}
