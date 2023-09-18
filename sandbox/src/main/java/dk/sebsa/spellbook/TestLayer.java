package dk.sebsa.spellbook;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.io.KeyPressedEvent;
import dk.sebsa.spellbook.marble.MarbleIM;
import dk.sebsa.spellbook.math.Rect;
import org.lwjgl.glfw.GLFW;

public class TestLayer extends Layer {
    private final DebugLayer debugLayer;
    private final GLFWWindow window;

    public TestLayer(EngineInitEvent e, DebugLayer debugLayer) {
        super(e.logger);
        this.debugLayer = debugLayer;
        this.window = ((Core) Spellbook.instance.getModules().get(0)).getWindow();
    }

    @Override
    protected void userEvent(UserEvent e) {
        log(e);

        if (e.eventType().equals(Event.EventType.ioKeyPressed)) {
            KeyPressedEvent keyEvent = (KeyPressedEvent) e;
            if (keyEvent.key == GLFW.GLFW_KEY_F11) window.fullscreen(!window.isFullscreen());
            if (keyEvent.key == GLFW.GLFW_KEY_F2) debugLayer.enabled = !debugLayer.enabled;
            if (keyEvent.key == GLFW.GLFW_KEY_F3) MarbleIM.font("Consolas", 100, 1);
        }
    }

    @Override
    public void render(Rect r) {
        MarbleIM.prepare();
        MarbleIM.label("Hello World!", 0, 0);
        MarbleIM.sprite(new Rect(0, 100, 100, 100), null);
        MarbleIM.unprepare();
    }
}
