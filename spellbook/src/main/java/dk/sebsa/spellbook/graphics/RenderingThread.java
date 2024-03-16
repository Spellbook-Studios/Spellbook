package dk.sebsa.spellbook.graphics;

import lombok.CustomLog;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Used to run graphtasks in a loop until asked to stop
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class RenderingThread implements Runnable {
    protected final List<GraphTask> taskQueue = Collections.synchronizedList(new LinkedList<>());
    private final AtomicBoolean stop = new AtomicBoolean(false);
    public final AtomicBoolean runningTask = new AtomicBoolean(false);

    @Override
    public void run() {
        logger.log("Graphics Thread MainLoop");

        while(!stop.get()) {
            while(!taskQueue.isEmpty()) {
                runningTask.set(true);

                try {
                    taskQueue.removeFirst().run();
                } catch (Exception e) {
                    logger.err("Exception in renderingthread", logger.stackTrace(e));
                }

                runningTask.set(false);
            }
        }
    }

    /**
     * When all current tasks are completed the rendering thread will stop
     */
    public void startCleanup() {
        stop.set(true);
        logger.trace("RenderingThread marked for cleanup");
    }

    /**
     * Adds a task to the back of the task queue
     * @param g Task to queue
     */
    public void queue(GraphTask g) {
        taskQueue.add(g);
    }
}
