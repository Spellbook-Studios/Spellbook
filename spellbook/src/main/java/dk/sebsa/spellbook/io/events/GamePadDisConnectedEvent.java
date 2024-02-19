package dk.sebsa.spellbook.io.events;

import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.io.GamePad;
import lombok.RequiredArgsConstructor;

/**
 * Occurs when a gamepad is disconnected
 * Occurs in the main loop
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class GamePadDisConnectedEvent extends Event {
    /**
     * The now disconnected gamepad
     */
    public final GamePad gamePad;

    @Override
    public EventType eventType() {
        return EventType.gamePadDisConnected;
    }
}
