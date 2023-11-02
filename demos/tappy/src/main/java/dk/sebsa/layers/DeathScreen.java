package dk.sebsa.layers;

import dk.sebsa.Tappy;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.io.KeyPressedEvent;
import dk.sebsa.spellbook.marble.FontType;
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
                marble.font((FontType) AssetManager.getAssetS("tappy/SAOWelcomeTT-Bold.ttf"), 82),
                null,
                null);
        if (otherRenderer == null) otherRenderer = marble.getMarbleIM(marble.font("Inter", 24), null, null);
        titleRenderer.prepare();
        titleRenderer.label("You Died", (r.width / 2) - ((float) titleRenderer.getFont().getStringWidth("You Died") / 2), 20);
        titleRenderer.unprepare();

        otherRenderer.prepare();
        otherRenderer.label("Press SPACE to restart", (r.width / 2) - ((float) otherRenderer.getFont().getStringWidth("Press SPACE to restart") / 2), 124);
        otherRenderer.unprepare();
    }
}
