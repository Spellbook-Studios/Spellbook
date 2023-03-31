package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that a user has released a keyboard key
 * @author sebsn
 * @since 0.0.1
 */
public class KeyReleasedEvent extends UserEvent {
    /**
     * The key pressed
     */
    public final int key;

    /**
     * @param key The key pressed
     */
    public KeyReleasedEvent(int key) {
        this.key = key;
    }

    @Override
    public EventType eventType() {
        return EventType.ioKeyReleased;
    }
}
