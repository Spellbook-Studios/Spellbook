package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.core.Core;

/**
 * Signifies to engine modules that they should load assets and prepare for the first frame
 * @author sebs
 * @since 0.0.1
 */
public class EngineLoadEvent extends Event {
    public final SpellbookCapabilities capabilities;
    public final AssetManager assetManager;
    public final Core moduleCore;

    public EngineLoadEvent(SpellbookCapabilities capabilities, AssetManager assetManager, Core moduleCore) {
        this.capabilities = capabilities;
        this.assetManager = assetManager;
        this.moduleCore = moduleCore;
    }

    @Override
    protected EventType eventType() {
        return EventType.engineLoad;
    }
}
