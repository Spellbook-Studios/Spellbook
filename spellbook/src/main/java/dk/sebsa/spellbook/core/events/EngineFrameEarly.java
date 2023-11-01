package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.FrameData;
import lombok.RequiredArgsConstructor;

/**
 * The hasppens before the frame happens
 * used eg. polling glfw events
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineFrameEarly extends Event {
    /**
     * Framedata for this frame
     */
    public final FrameData frameData;

    @Override
    public EventType eventType() {
        return EventType.engineFrameEarly;
    }
}
