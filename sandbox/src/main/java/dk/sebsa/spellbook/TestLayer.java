package dk.sebsa.spellbook;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.io.events.KeyPressedEvent;
import dk.sebsa.spellbook.marble.Marble;
import dk.sebsa.spellbook.marble.MarbleIMRenderer;
import dk.sebsa.spellbook.math.Rect;
import lombok.CustomLog;
import org.lwjgl.glfw.GLFW;

@CustomLog
public class TestLayer extends Layer {
    private final DebugLayer debugLayer;
    private final GLFWWindow window;
    private MarbleIMRenderer marble;

    public TestLayer(DebugLayer debugLayer) {
        this.debugLayer = debugLayer;
        this.window = ((Core) Spellbook.instance.getModules().get(0)).getWindow();
    }

    @Override
    protected void userEvent(UserEvent e) {
        log.log(e);

        if (e.eventType().equals(Event.EventType.ioKeyPressed)) {
            KeyPressedEvent keyEvent = (KeyPressedEvent) e;
            if (keyEvent.key == GLFW.GLFW_KEY_F11) window.fullscreen(!window.isFullscreen());
            if (keyEvent.key == GLFW.GLFW_KEY_F2) debugLayer.enabled = !debugLayer.enabled;
        }
    }

    @Override
    public void render(Marble marbleI, Rect r) {
        if (marble == null) marble = marbleI.getMarbleIM(null, null, null);
        marble.box(new Rect(0, 100, 100, 100));
        marble.boxSpecialLight(new Rect(0, 100, 50, 50));
        marble.boxSpecialDark(new Rect(50, 100, 50, 50));
        marble.label("Hello World!\nLife sure is fun, right?\nSphinx", new Rect(0,0,500,1000));
    }
}
