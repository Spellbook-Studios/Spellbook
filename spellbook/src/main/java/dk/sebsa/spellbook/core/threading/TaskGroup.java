package dk.sebsa.spellbook.core.threading;

import java.util.Collection;

/**
 * A collection of tasks that are running
 * Still not fully implemented
 *
 * @author sebs
 * @since 1.0.0
 */
public class TaskGroup {
    /**
     * Collection of tasks to run
     */
    public final Collection<Task> tasks;

    /**
     * Creates a TaskGroup with the provided tasks
     *
     * @param tasks Collection of tasks to run
     */
    public TaskGroup(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * @return True if all tasks are DONE otherwise false
     */
    public boolean isDone() {
        tasks.removeIf(task -> task.state == TaskState.DONE);
        return tasks.isEmpty();
    }
}
