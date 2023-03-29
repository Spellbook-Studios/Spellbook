package dk.sebsa.spellbook;

import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.math.Rect;

public class TestLayer extends Layer {
    public TestLayer(SpellbookLogger logger) {
        super(logger);
    }

    @Override
    protected void userEvent(UserEvent event) {
        log(event);
    }

    @Override
    public void render(Rect r) {

    }
}
