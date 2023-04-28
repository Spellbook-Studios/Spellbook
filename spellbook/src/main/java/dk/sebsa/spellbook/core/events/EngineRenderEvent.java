package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.io.GLFWWindow;

/**
 *  Now render the frame
 * @since 0.0.1
 * @author sebs
 */
public class EngineRenderEvent extends Event {
    /**
     * Current window to render to
     */
    public final GLFWWindow window;

    /**
     * Framedata for this frame
     */
    public final FrameData frameData;

    /**
     * @param frameData Framedata for this frame
     * @param window Window to render to
     */
    public EngineRenderEvent(FrameData frameData, GLFWWindow window) {
        this.window = window;
        this.frameData = frameData;
    }

    @Override
    public EventType eventType() {
        return EventType.engineRender;
    }
}
