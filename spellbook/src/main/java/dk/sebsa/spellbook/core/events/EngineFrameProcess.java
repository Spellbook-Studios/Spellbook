package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.FrameData;
import lombok.RequiredArgsConstructor;

/**
 * Now process input, and do main logical actions eg. entities locic
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineFrameProcess extends Event {
    /**
     * Framedata for this frame
     */
    public final FrameData frameData;

    @Override
    public EventType eventType() {
        return EventType.engineFrameProcess;
    }
}
