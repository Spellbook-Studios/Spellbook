package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that a user has released a keyboard key
 * @author sebsn
 * @since 0.0.1
 */
public class KeyReleasedEvent extends UserEvent {
    public final int key;

    public KeyReleasedEvent(int key) {
        this.key = key;
    }

    @Override
    protected EventType eventType() {
        return EventType.ioKeyReleased;
    }
}
