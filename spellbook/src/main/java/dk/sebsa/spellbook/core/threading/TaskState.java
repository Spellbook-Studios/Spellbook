package dk.sebsa.spellbook.core.threading;

/**
 * The state of a task
 *
 * @author sebs
 * @since 1.0.0
 */
public enum TaskState {
    CREATED,
    RUNNING,
    DONE,
    CANCELLED
}
