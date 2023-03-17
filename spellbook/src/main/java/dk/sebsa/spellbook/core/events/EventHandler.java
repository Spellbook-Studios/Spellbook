package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.io.GLFWWindow;

public interface EventHandler {
    @EventListener
    default void engineInit(EngineInitEvent e) {};
    @EventListener
    default void engineLoad(EngineLoadEvent e) {};
    @EventListener
    default void engineCleanup(EngineCleanupEvent e) {};
    @EventListener
    default void windowResized(GLFWWindow.WindowResizedEvent e) {};
}
