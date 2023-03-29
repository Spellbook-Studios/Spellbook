package dk.sebsa.spellbook.core.events;

/**
 * The hasppens before the frame happens
 * used eg. polling glfw events
 * @since 0.0.1
 * @author sebs
 */
public class EngineFrameEarly extends Event {
    @Override
    public EventType eventType() {
        return EventType.engineFrameEarly;
    }
}
