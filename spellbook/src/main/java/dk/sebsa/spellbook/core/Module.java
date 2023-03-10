package dk.sebsa.spellbook.core;

import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventHandler;

public interface Module extends EventHandler {
    public void init(EventBus eventBus);
    public void cleanup();
    public String name();
}
