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
        engineInit, engineLoad, engineCleanup,
        windowResized,
    }

}
