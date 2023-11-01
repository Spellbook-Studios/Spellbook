package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.FrameData;
import lombok.RequiredArgsConstructor;

/**
 * The frame has been rendered, cleanup and wait til next frame
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineFrameDone extends Event {
    /**
     * Framedata for this frame
     */
    public final FrameData frameData;

    @Override
    public EventType eventType() {
        return EventType.engineFrameDone;
    }
}
