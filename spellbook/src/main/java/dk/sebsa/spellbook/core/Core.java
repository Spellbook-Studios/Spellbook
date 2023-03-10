package dk.sebsa.spellbook.core;

import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventListener;

public class Core implements Module {
    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
    }

    @EventListener
    public void engineInit(Event e) {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public String name() {
        return "SpellbookCore";
    }
}
