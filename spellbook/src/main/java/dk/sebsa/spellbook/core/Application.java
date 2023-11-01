package dk.sebsa.spellbook.core;

import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.core.events.EventHandler;
import dk.sebsa.spellbook.core.events.LayerStack;

/**
 * A Spellbook Application
 * Applications are automatically registered as an eventhandler to the EventBus
 *
 * @author sebs
 * @since 1.0.0
 */
public interface Application extends EventHandler {
    /**
     * Get the name of the application
     *
     * @return The name of the application
     */
    String name();

    /**
     * Get the author of the application
     *
     * @return The author of the application
     */
    String author();

    /**
     * Get the version of the application
     *
     * @return The version of the application
     */
    String version();

    /**
     * Assembles the layerstack to be used
     *
     * @param e The engine init event
     * @return The final layerstack that should be used
     */
    LayerStack layerStack(EngineInitEvent e);
}
