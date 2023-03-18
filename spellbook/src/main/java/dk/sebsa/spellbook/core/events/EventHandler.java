package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.io.GLFWWindow;

public interface EventHandler {
    @EventListener
    default void engineInit(EngineInitEvent e) {}

    @EventListener
    default void engineLoad(EngineLoadEvent e) {}

    @EventListener
    default void engineCleanup(EngineCleanupEvent e) {}

    @EventListener
    default void windowResized(GLFWWindow.WindowResizedEvent e) {}

    @EventListener
    default void engineFrameEarly(EngineFrameEarly e) {}

    @EventListener
    default void engineFrameProcess(EngineFrameProcess e) {}

    @EventListener
    default void engineRender(EngineRenderEvent e) {}

    @EventListener
    default void engineFrameDone(EngineFrameDone e) {}
}
