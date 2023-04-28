package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.FrameData;

/**
 * The frame has been rendered, cleanup and wait til next frame
 * @since 0.0.1
 * @author sebs
 */
public class EngineFrameDone extends Event {
    /**
     * Framedata for this frame
     */
    public final FrameData frameData;

    /**
     * @param frameData Frame Data for this frame
     */
    public EngineFrameDone(FrameData frameData) {
        this.frameData = frameData;
    }

    @Override
    public EventType eventType() {
        return EventType.engineFrameDone;
    }
}
