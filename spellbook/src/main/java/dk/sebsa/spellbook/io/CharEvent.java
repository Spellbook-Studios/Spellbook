package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that the keyboard is sending a char event
 * @author sebsn
 * @since 0.0.1
 */
public class CharEvent extends UserEvent {
    public final int codePoint;

    public CharEvent(int codePoint) {
        this.codePoint = codePoint;
    }

    @Override
    protected EventType eventType() {
        return EventType.ioChar;
    }
}
