package dk.sebsa.spellbook.core.events;

public interface EventHandler {
    @EventListener
    default void engineInit(EngineInitEvent e) {};
    @EventListener
    default void engineLoad(EngineLoadEvent e) {};
    @EventListener
    default void engineCleanup(EngineCleanupEvent e) {};
}
