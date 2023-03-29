package dk.sebsa.spellbook;

import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.imgui.ImGUILayer;

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
