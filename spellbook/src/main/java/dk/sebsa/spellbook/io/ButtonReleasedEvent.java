package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;
import lombok.RequiredArgsConstructor;

/**
 * Tells the engine that a user has released a mouse button
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ButtonReleasedEvent extends UserEvent {
    /**
     * The button pressed
     * e.g. GLFW.GLFW_MOUSE_BUTTON_1
     */
    public final int button;
    
    @Override
    public EventType eventType() {
        return EventType.ioButtonReleased;
    }
}
