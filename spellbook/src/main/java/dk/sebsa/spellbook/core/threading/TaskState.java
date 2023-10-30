package dk.sebsa.spellbook.core.threading;

/**
 * The state of a task
 *
 * @author sebs
 * @since 1.0.0
 */
public enum TaskState {
    /**
     * The object is instantiated
     */
    CREATED,
    /**
     * The task is currently being executed
     */
    RUNNING,
    /**
     * The task has finished running
     */
    DONE,
    /**
     * IDFK Futures has a isCancalled() method
     */
    CANCELLED
}
