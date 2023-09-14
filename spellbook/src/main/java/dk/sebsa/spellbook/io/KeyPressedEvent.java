package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that a user has pressed a keyboard key
 * @author sebsn
 * @since 1.0.0
 */
public class KeyPressedEvent extends UserEvent {
    /**
     * The key pressed
     */
    public final int key;

    /**
     * @param key The key pressed
     */
    public KeyPressedEvent(int key) {
        this.key = key;
    }

    @Override
    public EventType eventType() {
        return EventType.ioKeyPressed;
    }
}
