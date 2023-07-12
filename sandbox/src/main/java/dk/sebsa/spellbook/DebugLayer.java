package dk.sebsa.spellbook;

import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.imgui.ImGUILayer;
import dk.sebsa.spellbook.io.KeyReleasedEvent;
import org.lwjgl.glfw.GLFW;

public class DebugLayer extends ImGUILayer {
    public DebugLayer(SpellbookLogger logger) {
        super(logger);
    }

    @Override
    protected void drawImGUI() {

    }

    @Override
    protected boolean disableDefaultWindows() {
        return false;
    }
}
