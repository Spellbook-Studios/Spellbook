package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.io.events.GamePadConnectedEvent;
import dk.sebsa.spellbook.io.events.GamePadDisConnectedEvent;

/**
 * Has functions for all engine events
 * To use register object with eventbus and annotate with @EventListener
 *
 * @author sebs
 * @since 1.0.0
 */
public interface EventHandler {
    /**
     * Tells the engine that it is time to initialize it's component
     *
     * @param e The event
     */
    @EventListener
    default void engineInit(EngineInitEvent e) {
    }

    /**
     * The engine should now begin to load in resources and prepare for first frame
     *
     * @param e The event
     */
    @EventListener
    default void engineLoad(EngineLoadEvent e) {
    }

    /**
     * Everything is initilized and all resources are ready
     * The engine can now create and prepare stuff for the first frame
     *
     * @param e The event
     */
    @EventListener
    default void engineFirstFrame(EngineFirstFrameEvent e) {
    }

    /**
     * @param e The event
     */
    @EventListener
    default void engineCleanup(EngineCleanupEvent e) {
    }

    /**
     * The engine should destroy itself leaving no traces in memory :D
     *
     * @param e The event
     */
    @EventListener
    default void windowResized(GLFWWindow.WindowResizedEvent e) {
    }

    /**
     * The engine should just prepare for the frame, eg. get input
     *
     * @param e The event
     */
    @EventListener
    default void engineFrameEarly(EngineFrameEarly e) {
    }

    /**
     * Now process input, and do main logical actions eg. entities logic
     *
     * @param e The event
     */
    @EventListener
    default void engineFrameProcess(EngineFrameProcess e) {
    }

    /**
     * Now render the frame
     *
     * @param e The event
     */
    @EventListener
    default void engineRender(EngineRenderEvent e) {
    }

    /**
     * The frame has been rendered, cleanup and wait til next frame
     *
     * @param e The event
     */
    @EventListener
    default void engineFrameDone(EngineFrameDone e) {
    }

    /**
     * The engine is now ready to build the first scene
     *
     * @param e The event
     */
    @EventListener
    default void engineCreateFirstScene(EngineCreateFirstSceneEvent e) {
    }

    /**
     * The engine needs a new RenderingPipeline
     *
     * @param e The event
     */
    @EventListener
    default void engineBuildRenderPipeline(EngineBuildRenderPipelineEvent e) {
    }

    /**
     * The engine needs a new LayerStack
     *
     * @param e The event
     */
    @EventListener
    default void engineBuildLayerStack(EngineBuildLayerStackEvent e) {
    }

    /**
     * Occurs when a gamepad is connected
     * Either during FirstFrame or in the main loop
     *
     * @param e The event
     */
    @EventListener
    default void gamePadConnected(GamePadConnectedEvent e) {
    }

    /**
     * Occurs when a gamepad is disconnected
     * Occurs in the main loop
     *
     * @param e The event
     */
    @EventListener
    default void gamePadDisConnected(GamePadDisConnectedEvent e) {
    }
}
