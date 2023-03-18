package dk.sebsa.spellbook.core.events;

/**
 * Now process input, and do main logical actions eg. entities locic
 * @since 0.0.1
 * @author sebs
 */
public class EngineFrameProcess extends Event {
    @Override
    protected EventType eventType() {
        return EventType.engineFrameProcess;
    }
}
