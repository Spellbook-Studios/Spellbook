package dk.sebsa.layers;

import dk.sebsa.Tappy;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.io.events.KeyPressedEvent;
import dk.sebsa.spellbook.marble.Marble;
import dk.sebsa.spellbook.marble.MarbleIMRenderer;
import dk.sebsa.spellbook.math.Rect;
import org.lwjgl.glfw.GLFW;

public class DeathScreen extends Layer {
    private MarbleIMRenderer titleRenderer;
    private MarbleIMRenderer otherRenderer;
    private MarbleIMRenderer ui;

    @Override
    protected void userEvent(UserEvent event) {
        if (event.eventType().equals(Event.EventType.ioKeyPressed)) {
            if (((KeyPressedEvent) event).key == GLFW.GLFW_KEY_SPACE)
                Tappy.instance.start();
        }
    }

    @Override
    protected void render(Marble marble, Rect r) {
        if (titleRenderer == null) titleRenderer = marble.getMarbleIM(
                marble.font(new Identifier("tappy", "SAOWelcomeTT-Bold.ttf"), 82),
                null);
        if (otherRenderer == null)
            otherRenderer = marble.getMarbleIM(marble.font(new Identifier("spellbook", "fonts/Inter.ttf"), 24), null);
        titleRenderer.labelCenterH("You Died", new Rect(0, 100, r.width, 124));

        otherRenderer.labelCenterH("Press SPACE to restart", new Rect(0, 204, r.width, 242));
    }
}
