package dk.sebsa.spellbook.core.events;

/**
 * Events represents to things
 * User Event: A representation of an event occurring inside Spellbook (stored and queued)
 * Engine Event: A call from the core, telling the engine that it's time to do X (blocking and immediate)
 * @author sebsn
 * @since 0.0.1
 */
public abstract class Event {
    protected abstract EventType eventType();

    /**
     * A enum denoting what triggered an event
     * @author sebsn
     * @since 0.0.1
     */
    public enum EventType {
        engineInit, // Tells the engine that it is time to initialize it's component
        engineLoad, // The engine should now begin to load in resources and prepare for first frame

        engineFrameEarly, // The engine should just prepare for the frame, eg. get input
        engineFrameProcess, // Now process input, and do main logical actions eg. entities locic
        engineRender, // Now render the frame
        engineFrameDone, // The frame has been rendered, cleanup and wait til next frame

        engineCleanup, // The engine should destroy itself leaving no traces in memory :D
        windowResized, // Tells the engine that the window was resized

        ioKeyPressed, // Tells the engine that a user has pressed a keyboard key
        ioKeyReleased, // Tells the engine that a user has released a keyboard key
        ioChar, // Tells the engine that the keyboard is sending a char event
        ioButtonPressed, // Tells the engine that a user has pressed a mouse button
        ioButtonReleased, // Tells the engine that a user has released a mouse button
        ioMouseMove, // Tells the engine that the mouse has moved
        ioMouseScroll, // Tells the engine that the mouse scrolled
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + eventType() +
                '}';
    }
}
