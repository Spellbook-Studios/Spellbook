package dk.sebsa.spellbook.core.threading;

/**
 * The state of a task
 *
 * @author sebs
 * @since 1.0.0
 */
public enum TaskState {
    /**
     * The task has not been started
     */
    CREATED,
    /**
     * The task is currently running
     */
    RUNNING,
    /**
     * The task has finished running
     */
    DONE,
    /**
     * Not Yet Implemented
     */
    CANCELLED
}
