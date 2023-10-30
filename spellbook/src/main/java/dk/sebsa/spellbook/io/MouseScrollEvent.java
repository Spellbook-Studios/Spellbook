package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that the mouse scrolled
 *
 * @author sebs
 * @since 1.0.0
 */
public class MouseScrollEvent extends UserEvent {
    /**
     * The offset X scroll
     */
    public final double offsetX;
    /**
     * The offset Y scroll
     */
    public final double offsetY;

    /**
     * @param offsetX Offset X scroll
     * @param offsetY Offset Y scroll
     */
    public MouseScrollEvent(double offsetX, double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public EventType eventType() {
        return EventType.ioMouseScroll;
    }
}
