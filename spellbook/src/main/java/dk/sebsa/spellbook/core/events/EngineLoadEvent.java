package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.asset.AssetManager;

/**
 * Signifies to engine modules that they should load assets and prepare for the first frame
 * @author sebs
 * @since 0.0.1
 */
public class EngineLoadEvent extends Event {
    public final SpellbookCapabilities capabilities;
    public final AssetManager assetManager;

    public EngineLoadEvent(SpellbookCapabilities capabilities, AssetManager assetManager) {
        this.capabilities = capabilities;
        this.assetManager = assetManager;
    }

    @Override
    protected EventType eventType() {
        return EventType.engineLoad;
    }
}
