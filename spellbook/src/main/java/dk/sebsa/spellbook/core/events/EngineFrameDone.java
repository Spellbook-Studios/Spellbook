package dk.sebsa.spellbook.core.events;

/**
 * The frame has been rendered, cleanup and wait til next frame
 * @since 0.0.1
 * @author sebs
 */
public class EngineFrameDone extends Event {
    @Override
    protected EventType eventType() {
        return EventType.engineFrameDone;
    }
}
