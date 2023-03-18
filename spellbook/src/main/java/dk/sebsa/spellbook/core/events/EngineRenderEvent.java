package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.io.GLFWWindow;

/**
 *  Now render the frame
 * @since 0.0.1
 * @author sebs
 */
public class EngineRenderEvent extends Event {
    public final GLFWWindow window;

    public EngineRenderEvent(GLFWWindow window) {
        this.window = window;
    }

    @Override
    protected EventType eventType() {
        return EventType.engineRender;
    }
}
