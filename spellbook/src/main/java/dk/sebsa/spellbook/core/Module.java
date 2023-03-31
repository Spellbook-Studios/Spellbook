package dk.sebsa.spellbook.core;

import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventHandler;

/**
 * A module of the Spellbook engine
 * @since 0.0.1
 * @author sebs
 */
public interface Module extends EventHandler {
    /**
     * Before the engine starts
     * Mostly used to register listeners from the eventBus
     * @param eventBus The eventbus that the module can register itself to
     */
    void init(EventBus eventBus);

    /**
     * Destroy the module and close all streams...
     */
    void cleanup();

    /**
     * Gets the name of the module
     * @return The name of the module
     */
    String name();
}
