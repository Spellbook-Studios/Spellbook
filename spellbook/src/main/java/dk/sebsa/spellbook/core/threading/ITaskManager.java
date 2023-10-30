package dk.sebsa.spellbook.core.threading;

import java.util.function.Consumer;

/**
 * Manages running tasks
 *
 * @author sebs
 * @since 1.0.0
 */
public interface ITaskManager {
    /**
     * Called once a frame so the manager can process notifying tasks
     */
    void handleReturn();

    /**
     * Runs a task
     *
     * @param task The task to run
     * @return The task now running
     */
    Task run(Task task);

    /**
     * Schedules an entire TaskGroup
     *
     * @param tasks Taskgroup to run
     * @return The TaskGroup now running
     */
    TaskGroup run(TaskGroup tasks);

    /**
     * Runs a task and notifies a consumer when finished
     *
     * @param task     Task to run
     * @param consumer Consumer of the task
     * @return The task now running
     */
    Task runNotifyOnFinish(Task task, Consumer<Task> consumer);
}
