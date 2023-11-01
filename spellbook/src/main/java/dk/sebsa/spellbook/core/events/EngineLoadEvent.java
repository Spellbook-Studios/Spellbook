package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.core.SpellbookLogger;
import lombok.RequiredArgsConstructor;

/**
 * Signifies to engine modules that they should load assets and prepare for the first frame
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineLoadEvent extends Event {
    /**
     * Spellbook capabilities
     */
    public final SpellbookCapabilities capabilities;
    /**
     * The asset manager instance
     */
    public final AssetManager assetManager;
    /**
     * The main application
     */
    public final Application app;
    /**
     * The Core module
     */
    public final Core moduleCore;
    /**
     * The main spellbook logger
     */
    public final SpellbookLogger logger;

    @Override
    public EventType eventType() {
        return EventType.engineLoad;
    }
}
