package dk.sebsa.spellbook.io.events;

import dk.sebsa.spellbook.core.events.UserEvent;
import lombok.RequiredArgsConstructor;

/**
 * Tells the engine that the mouse has moved
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class MouseMoveEvent extends UserEvent {
    /**
     * The current X position of the mouse
     */
    public final float mouseX;
    /**
     * The current Y position of the mouse
     */
    public final float mouseY;

    @Override
    public EventType eventType() {
        return EventType.ioMouseMove;
    }
}
