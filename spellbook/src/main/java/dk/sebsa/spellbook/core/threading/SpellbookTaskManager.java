package dk.sebsa.spellbook.core.threading;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Runs tasks using Java virtual threads
 *
 * @author sebs
 * @since 1.0.0
 */
public class SpellbookTaskManager implements ITaskManager {
    private final ExecutorService threadPool;
    private final Map<Task, Consumer<Task>> tasksRunning = new HashMap<>();

    /**
     * Creates a SpellbookTaskManager using the virtualThreadPerTaskExecutor executor
     */
    public SpellbookTaskManager() {
        ThreadFactory factory = Thread.ofVirtual().name("VThread-", 0L).factory();
        threadPool = Executors.newThreadPerTaskExecutor(factory);
    }

    @Override
    public void handleReturn() {
        for (Iterator<Task> tasks = tasksRunning.keySet().iterator(); tasks.hasNext(); )
            for (Task t : tasksRunning.keySet()) {
                if (t.state == TaskState.DONE) {
                    tasksRunning.get(t).accept(t);
                    tasks.remove();
                }
            }
    }

    @Override
    public Task run(Task task) {
        threadPool.submit(task);
        return task;
    }

    @Override
    public TaskGroup run(TaskGroup tasks) {
        for (Task t : tasks.tasks) run(t);
        return tasks;
    }

    @Override
    public Task runNotifyOnFinish(Task task, Consumer<Task> consumer) {
        tasksRunning.put(run(task), consumer);
        return task;
    }

    @Override
    public void shutdown() {
        threadPool.shutdown();
    }

    @Override
    public void shutdownNow() {
        threadPool.shutdownNow();
    }

    @Override
    public boolean awaitFinish(long timeout, TimeUnit timeUnit) {
        try {
            return threadPool.awaitTermination(timeout, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }
}
