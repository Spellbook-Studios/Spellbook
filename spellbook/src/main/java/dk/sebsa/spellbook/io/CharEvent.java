package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;
import lombok.RequiredArgsConstructor;

/**
 * Tells the engine that the keyboard is sending a char event
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class CharEvent extends UserEvent {
    /**
     * The code point of the charater
     * e.g. GLFW.GLFW_KEY_F3
     */
    public final int codePoint;

    @Override
    public EventType eventType() {
        return EventType.ioChar;
    }
}
