package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that the mouse scrolled
 * @author sebsn
 * @since 1.0.0
 */
public class MouseScrollEvent extends UserEvent {
    public final double offsetX, offsetY;

    public MouseScrollEvent(double offsetX, double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public EventType eventType() {
        return EventType.ioMouseScroll;
    }
}
