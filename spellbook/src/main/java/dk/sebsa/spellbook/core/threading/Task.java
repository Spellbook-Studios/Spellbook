package dk.sebsa.spellbook.core.threading;

import dk.sebsa.spellbook.math.Time;

/**
 * An executable task which can be completed asynchronously from the rest of the Spellbook engine
 *
 * @author sebs
 * @since 1.0.0
 */
public abstract class Task implements Runnable {
    /**
     * The current state of the task
     */
    public TaskState state = TaskState.CREATED;

    /**
     * @return The name of the task
     */
    public abstract String name();

    /**
     * Executes the task
     */
    public abstract void execute();

    @Override
    public void run() {
        state = TaskState.RUNNING;
        execute();
        state = TaskState.DONE;
    }

    /**
     * The time the task was instantiated / the time the task has been running
     */
    long startTime = Time.getTime();
}
