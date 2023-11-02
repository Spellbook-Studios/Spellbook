package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.core.threading.DynamicTaskGroup;
import lombok.RequiredArgsConstructor;

/**
 * Signifies to engine modules that they should load assets and prepare for the first frame
 * It is recommended if possible to do the work in threads using the task group provided
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
     * Use this to schedule tasks to run, the event is only over when all tasks in this group is completed
     */
    public final DynamicTaskGroup tasks;

    @Override
    public EventType eventType() {
        return EventType.engineLoad;
    }
}
