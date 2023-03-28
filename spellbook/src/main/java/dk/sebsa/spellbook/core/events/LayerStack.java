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
    public final List<Layer> stack;

    private LayerStack(List<Layer> stack) {
        this.stack = stack;
    }

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

    public static class LayerStackBuilder {
        private List<Layer> stack = new ArrayList<>();

        public LayerStack.LayerStackBuilder appendLayer(Layer layer) { stack.add(layer); return this; }

        public LayerStack build() {
            LayerStack layerStack = new LayerStack(stack);
            stack = new ArrayList<>();

            return layerStack;
        }
    }
}
