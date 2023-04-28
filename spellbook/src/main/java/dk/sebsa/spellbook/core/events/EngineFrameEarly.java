package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.FrameData;

/**
 * The hasppens before the frame happens
 * used eg. polling glfw events
 * @since 0.0.1
 * @author sebs
 */
public class EngineFrameEarly extends Event {
    /**
     * Framedata for this frame
     */
    public final FrameData frameData;

    /**
     * @param frameData Framedata for this frame
     */
    public EngineFrameEarly(FrameData frameData) {
        this.frameData = frameData;
    }

    @Override
    public EventType eventType() {
        return EventType.engineFrameEarly;
    }
}
