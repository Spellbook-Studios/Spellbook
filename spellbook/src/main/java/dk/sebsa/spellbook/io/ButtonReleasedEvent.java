package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that a user has released a mouse button
 * @author sebsn
 * @since 1.0.0
 */
public class ButtonReleasedEvent extends UserEvent {
    public final int button;

    public ButtonReleasedEvent(int button) {
        this.button = button;
    }

    @Override
    public EventType eventType() {
        return EventType.ioButtonReleased;
    }
}
