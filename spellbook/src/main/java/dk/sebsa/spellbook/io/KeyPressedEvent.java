package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;
import lombok.RequiredArgsConstructor;

/**
 * Tells the engine that a user has pressed a keyboard key
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class KeyPressedEvent extends UserEvent {
    /**
     * The key pressed
     */
    public final int key;

    @Override
    public EventType eventType() {
        return EventType.ioKeyPressed;
    }
}
