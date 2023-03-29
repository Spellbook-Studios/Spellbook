package dk.sebsa.spellbook;

import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.io.KeyReleasedEvent;
import dk.sebsa.spellbook.math.Rect;
import org.lwjgl.glfw.GLFW;

public class TestLayer extends Layer {
    private final DebugLayer debugLayer;

    public TestLayer(SpellbookLogger logger, DebugLayer debugLayer) {
        super(logger);
        this.debugLayer = debugLayer;
    }

    @Override
    protected void userEvent(UserEvent e) {
        log(e);

        if(e.eventType().equals(Event.EventType.ioKeyReleased)) {
            KeyReleasedEvent keyEvent = (KeyReleasedEvent) e;
            if(keyEvent.key == GLFW.GLFW_KEY_F2) debugLayer.enabled = !debugLayer.enabled;
        }
    }

    @Override
    public void render(Rect r) {

    }
}
