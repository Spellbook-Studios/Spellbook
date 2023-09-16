package dk.sebsa.spellbook.core.threading;

import dk.sebsa.spellbook.math.Time;

/**
 * A executable task
 *
 * @author sebs
 * @since 1.0.0
 */
public interface Task {
    /**
     * @return The name of the task
     */
    String name();

    /**
     * Runs the task on the current thread
     */
    void run();

    /**
     * The time the task was instantiated / first runned
     */
    long startTime = Time.getTime();
}
