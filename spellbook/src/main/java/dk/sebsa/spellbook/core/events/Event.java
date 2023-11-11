package dk.sebsa.spellbook.core.events;

import lombok.ToString;

/**
 * Events represents to things
 * User Event: A representation of an event occurring inside Spellbook (stored and queued)
 * Engine Event: A call from the core, telling the engine that it's time to do X (blocking and immediate)
 *
 * @author sebs
 * @since 1.0.0
 */
@ToString
public abstract class Event {
    /**
     * Gets the type of event
     *
     * @return The type of the event
     */
    public abstract EventType eventType();

    /**
     * A enum denoting what triggered an event
     *
     * @author sebs
     * @since 1.0.0
     */
    public enum EventType {
        /**
         * Tells the engine that it is time to initialize it's component
         */
        engineInit,

        /**
         * The engine should now begin to load in resources
         */
        engineLoad,

        /**
         * The engine is now ready to build the first scene
         */
        engineCreateFirstScene,

        /**
         * Engine needs a layerstack
         */
        engineBuildLayerStack,

        /**
         * Engine needs a render pipeline
         */
        engineBuildRenderPipeline,

        /**
         * Everything is initilized and all resources are ready
         * The engine can now create and prepare stuff for the first frame
         */
        engineFirstFrame,

        /**
         * The engine should just prepare for the frame, eg. get input
         */
        engineFrameEarly,

        /**
         * Now process input, and do main logical actions eg. entities logic
         */
        engineFrameProcess,

        /**
         * Now render the frame
         */
        engineRender,

        /**
         * The frame has been rendered, cleanup and wait til next frame
         */
        engineFrameDone,

        /**
         * The engine should destroy itself leaving no traces in memory :D
         */
        engineCleanup,

        /**
         * Tells the engine that the window was resized
         */
        windowResized,


        /**
         * Tells the engine that a user has pressed a keyboard key
         */
        ioKeyPressed,

        /**
         * Tells the engine that a user has released a keyboard key
         */
        ioKeyReleased,

        /**
         * Tells the engine that the keyboard is sending a char event
         */
        ioChar, //

        /**
         * Tells the engine that a user has pressed a mouse button
         */
        ioButtonPressed,

        /**
         * Tells the engine that a user has released a mouse button
         */
        ioButtonReleased,

        /**
         * Tells the engine that the mouse has moved
         */
        ioMouseMove,

        /**
         * Tells the engine that the mouse scrolled
         */
        ioMouseScroll,

        /**
         * Occurs when a gamepad is connected
         * Either during FirstFrame or in the main loop
         */
        gamePadConnected,

        /**
         * Occurs when a gamepad is disconnected
         * Occurs in the main loop
         */
        gamePadDisConnected,
    }
}
