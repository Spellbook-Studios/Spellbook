package dk.sebsa.spellbook.io.events;

import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.io.GamePad;
import lombok.RequiredArgsConstructor;

/**
 * Occurs when a gamepad is connected
 * Either during FirstFrame or in the main loop
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class GamePadConnectedEvent extends Event {
    /**
     * The connected gamepad
     */
    public final GamePad gamePad;

    @Override
    public EventType eventType() {
        return EventType.gamePadConnected;
    }
}
