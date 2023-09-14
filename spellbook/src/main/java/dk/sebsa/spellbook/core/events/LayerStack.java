package dk.sebsa.spellbook.core.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * A vertical stack of layers
 * where events can travel from top to bottom,
 * and it is possible for layers to catch and stop the events (only accounts for user events)
 *
 * @author sebs
 * @since 1.0.0
 */
public class LayerStack {
    /**
     * The actual layerstack
     * Element 0 is at the bottom
     * and element stack.size()-1 is the top
     */
    public final List<Layer> stack;

    private LayerStack(List<Layer> stack) {
        this.stack = stack;
    }

    /**
     * Polls events from event queue and handles them through layerstack
     * @param events Events to process
     */
    public void handleEvents(Queue<UserEvent> events) {
        while(!events.isEmpty()) {
            UserEvent e = events.poll();

            for (Layer layer : stack) {
                if (!layer.enabled) continue;
                if (e.isBlocked()) break;
                layer.userEvent(e);
            }
        }
    }

    /**
     * Builds layerstacks
     * @author sebsn
     * @since 1.0.0
     */
    public static class LayerStackBuilder {
        private List<Layer> stack = new ArrayList<>();

        /**
         * Appends layer to top of layerstack
         * @param layer Layer to append
         * @return this
         */
        public LayerStack.LayerStackBuilder appendLayer(Layer layer) { stack.add(layer); return this; }

        /**
         * Assembles the final layerstack with the layers append in appendLayer(Layer layer)
         * @return The final layerstack
         */
        public LayerStack build() {
            LayerStack layerStack = new LayerStack(stack);
            stack = new ArrayList<>();

            return layerStack;
        }
    }
}
