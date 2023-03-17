package dk.sebsa.spellbook.core;

import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventHandler;

public interface Module extends EventHandler {
    void init(EventBus eventBus);
    void cleanup();
    String name();
}
