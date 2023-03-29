package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.opengl.RenderPipeline;

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
    public EventType eventType() {
        return EventType.engineRender;
    }
}
