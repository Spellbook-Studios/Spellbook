package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.math.Vector2f;

/**
 * Tells the engine that the mouse has moved
 * @author sebsn
 * @since 1.0.0
 */
public class MouseMoveEvent extends UserEvent {
    /**
     * The current position of the mouse
     */
    public final float mouseX, mouseY;

    /**
     * The offset from the position of the mouse last frame
     */
    public final float offsetX, offsetY;

    /**
     *
     * @param mouseX The current mouse position x
     * @param mouseY The current mouse position y
     * @param offsetX The offset from the position of the mouse last frame X
     * @param offsetY The offset from the position of the mouse last frame Y
     */
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
