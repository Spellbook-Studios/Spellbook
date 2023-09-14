package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.FrameData;

/**
 * Now process input, and do main logical actions eg. entities locic
 * @since 1.0.0
 * @author sebs
 */
public class EngineFrameProcess extends Event {
    /**
     * Framedata for this frame
     */
    public final FrameData frameData;

    /**
     * @param frameData Framedata for this frame
     */
    public EngineFrameProcess(FrameData frameData) {
        this.frameData = frameData;
    }

    @Override
    public EventType eventType() {
        return EventType.engineFrameProcess;
    }
}
