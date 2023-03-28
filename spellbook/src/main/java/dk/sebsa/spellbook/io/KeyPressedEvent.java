package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that a user has pressed a keyboard key
 * @author sebsn
 * @since 0.0.1
 */
public class KeyPressedEvent extends UserEvent {
    public final int key;

    public KeyPressedEvent(int key) {
        this.key = key;
    }

    @Override
    protected EventType eventType() {
        return EventType.ioKeyPressed;
    }
}
