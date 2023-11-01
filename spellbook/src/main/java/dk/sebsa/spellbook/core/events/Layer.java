package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.marble.Marble;
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
     * Handles user events
     * The layer should use event.block() if the event is to be handled at this ui layer
     *
     * @param event The userEvent
     */
    protected abstract void userEvent(UserEvent event);


    /**
     * Renders this layer to the current FBO (if the layer is enabled)
     *
     * @param marble The marble GUI module instance
     * @param r      Surface to render to
     */
    public void ensureRender(Marble marble, Rect r) { // TODO: I hate this if check
        if (enabled) render(marble, r);
    }

    /**
     * Render the UI to the current framebuffer
     *
     * @param marble The marble GUI module instance
     * @param r      Surface to render to
     */
    protected abstract void render(Marble marble, Rect r);
}
