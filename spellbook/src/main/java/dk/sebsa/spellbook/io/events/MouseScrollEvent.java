package dk.sebsa.spellbook.io.events;

import dk.sebsa.spellbook.core.events.UserEvent;
import lombok.RequiredArgsConstructor;

/**
 * Tells the engine that the mouse scrolled
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class MouseScrollEvent extends UserEvent {
    /**
     * The offset X scroll
     */
    public final double offsetX;
    /**
     * The offset Y scroll
     */
    public final double offsetY;

    @Override
    public EventType eventType() {
        return EventType.ioMouseScroll;
    }
}
