package dk.sebsa.spellbook.core.threading;

import dk.sebsa.spellbook.math.Time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    /**
     * The time the task was instantiated / the time the task has been running
     */
    public final long startTime = Time.getTime();

    /**
     * Instantiates a new taskgroup builder
     *
     * @return a blank taskgroup builder
     */
    public static TaskGroupBuilder builder() {
        return new TaskGroupBuilder();
    }

    /**
     * Builds TaskGroups
     *
     * @author sebs
     * @since 1.0.0
     */
    public static class TaskGroupBuilder {
        private final List<Task> tasks = new ArrayList<>();

        /**
         * Adds a task to the taskgroup
         *
         * @param t Task to add
         * @return this
         */
        public TaskGroupBuilder addTask(Task t) {
            tasks.add(t);
            return this;
        }

        /**
         * Assembles a taskgroup with the tasks provided
         *
         * @return new TaskGroup
         */
        public TaskGroup build() {
            return new TaskGroup(tasks);
        }
    }
}
