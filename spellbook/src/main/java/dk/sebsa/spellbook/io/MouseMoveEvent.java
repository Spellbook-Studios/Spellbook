package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.math.Vector2f;

/**
 * Tells the engine that the mouse has moved
 * @author sebsn
 * @since 0.0.1
 */
public class MouseMoveEvent extends UserEvent {
    public final float mouseX, mouseY;
    public final float offsetX, offsetY;

    public MouseMoveEvent(float mouseX, float mouseY, float offsetX, float offsetY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public EventType eventType() {
        return EventType.ioMouseMove;
    }
}
