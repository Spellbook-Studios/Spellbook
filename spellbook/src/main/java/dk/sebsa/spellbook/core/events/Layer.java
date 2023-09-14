package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.math.Rect;

/**
 * Handles UI user events, and can also subscribe to engine events
 * User events are sent down from the layerstack from the top, to the bottom
 * Engine Events can be subscribed to by annotating with @EventListener
 *
 * @author sebs
 * @since 1.0.0
 */
public abstract class Layer implements EventHandler {
    /**
     * Weather the layers is accepting events and is being rendered
     */
    public boolean enabled = true;
    /**
     * Logger that the layer should use
     */
    protected final ClassLogger logger;

    /**
     * Logs an object to the ClassLogger
     * @param o Object to log
     */
    protected void log(Object o) { logger.log(o.toString()); }

    /**
     * @param logger The main Spellbook logger
     */
    public Layer(SpellbookLogger logger) {
        this.logger = new ClassLogger(this, logger);
    }


    /**
     * Handles user events
     * The layer should use event.block() if the event is to be handled at this ui layer
     *
     * @param event The userEvent
     */
    protected abstract void userEvent(UserEvent event);


    /**
     * Renders this layer to the current FBO (if the layer is enabled)
     * @param r Surface to render to
     */
    public void ensureRender(Rect r) { // TODO: I hate this if check
        if(enabled) render(r);
    }

    /**
     * Render the UI to the current framebuffer
     * @param r Surface to render to
     */
    protected abstract void render(Rect r);
}
