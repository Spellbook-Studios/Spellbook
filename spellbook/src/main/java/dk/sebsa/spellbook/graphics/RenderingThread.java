package dk.sebsa.spellbook.graphics;

import lombok.CustomLog;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@CustomLog
public class RenderingThread implements Runnable {
    public final AtomicBoolean stop = new AtomicBoolean(false);
    protected final List<GraphTask> taskQueue = Collections.synchronizedList(new LinkedList<>());

    @Override
    public void run() {
        logger.log("Graphics Thread MainLoop");

        while(!stop.get()) {
            while(!taskQueue.isEmpty()) {
                taskQueue.removeFirst().run();
            }
        }
    }

    public void startCleanup() {
        stop.set(true);
        logger.trace("RenderingThread marked for cleanup");
    }

    public void queue(GraphTask g) {
        taskQueue.add(g);
    }
}
