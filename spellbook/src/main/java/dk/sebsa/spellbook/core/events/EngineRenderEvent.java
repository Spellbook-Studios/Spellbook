package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.io.GLFWWindow;
import lombok.RequiredArgsConstructor;

/**
 * Now render the frame
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineRenderEvent extends Event {
    /**
     * Framedata for this frame
     */
    public final FrameData frameData;

    /**
     * Current window to render to
     */
    public final GLFWWindow window;

    @Override
    public EventType eventType() {
        return EventType.engineRender;
    }
}
