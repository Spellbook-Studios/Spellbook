package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.core.SpellbookLogger;

/**
 * Signifies to engine modules that they should load assets and prepare for the first frame
 * @author sebs
 * @since 1.0.0
 */
public class EngineLoadEvent extends Event {
    public final SpellbookCapabilities capabilities;
    public final AssetManager assetManager;
    public final Application app;
    public final Core moduleCore;
    public final SpellbookLogger logger;

    public EngineLoadEvent(SpellbookCapabilities capabilities, AssetManager assetManager, Application app, Core moduleCore, SpellbookLogger logger) {
        this.capabilities = capabilities;
        this.assetManager = assetManager;
        this.app = app;
        this.moduleCore = moduleCore;
        this.logger = logger;
    }

    @Override
    public EventType eventType() {
        return EventType.engineLoad;
    }
}
