package dk.sebsa.spellbook.io;

import dk.sebsa.spellbook.core.events.UserEvent;

/**
 * Tells the engine that a user has pressed a mouse button
 *
 * @author sebs
 * @since 1.0.0
 */
public class ButtonPressedEvent extends UserEvent {
    /**
     * The button pressed
     * e.g. GLFW.GLFW_MOUSE_BUTTON_1
     */
    public final int button;

    /**
     * @param button The button pressed
     */
    public ButtonPressedEvent(int button) {
        this.button = button;
    }

    @Override
    public EventType eventType() {
        return EventType.ioButtonPressed;
    }
}
