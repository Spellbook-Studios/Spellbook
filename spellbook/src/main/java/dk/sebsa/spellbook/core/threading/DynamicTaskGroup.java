package dk.sebsa.spellbook.core.threading;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.math.Time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection of tasks that are running
 * Dynamic Task Groups can register new tasks dynamically and track its automatically
 *
 * @author sebs
 * @since 1.0.0
 */
public class DynamicTaskGroup {
    private final Collection<Task> runningTasks;

    /**
     * Creates a DynamicTaskGroup with the provided tasks
     *
     * @param tasks Collection of the initial tasks
     */
    public DynamicTaskGroup(Collection<Task> tasks) {
        this.runningTasks = tasks;
        for (Task t : runningTasks) {
            Spellbook.instance.getTaskManager().run(t);
        }
    }

    /**
     * Adds a tasks which is set to be executed immediately
     *
     * @param tasks Tasks to add
     */
    public void addTask(Task... tasks) {
        for (Task t : tasks) {
            Spellbook.instance.getTaskManager().run(t);
            runningTasks.add(t);
        }
    }

    /**
     * @return True if all tasks are DONE otherwise false
     */
    public boolean isDone() {
        runningTasks.removeIf(task -> task.state == TaskState.DONE);
        return runningTasks.isEmpty();
    }

    /**
     * The time the task was instantiated
     */
    public final long startTime = Time.getTime();

    /**
     * Instantiates a new DynamicTaskGroup builder
     *
     * @return a blank DynamicTaskGroup builder
     */
    public static DynamicTaskGroupBuilder builder() {
        return new DynamicTaskGroupBuilder();
    }

    /**
     * Builds DynamicTaskGroups
     *
     * @author sebs
     * @since 1.0.0
     */
    public static class DynamicTaskGroupBuilder {
        private final List<Task> tasks = new ArrayList<>();

        /**
         * Adds a task to the taskgroup
         *
         * @param t Task to add
         * @return this
         */
        public DynamicTaskGroupBuilder addTask(Task t) {
            tasks.add(t);
            return this;
        }

        /**
         * Assembles a taskgroup with the tasks provided
         *
         * @return new TaskGroup with initials tasks
         */
        public DynamicTaskGroup build() {
            return new DynamicTaskGroup(tasks);
        }
    }
}
