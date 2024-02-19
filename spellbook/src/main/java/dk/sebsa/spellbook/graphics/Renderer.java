package dk.sebsa.spellbook.graphics;

import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import lombok.CustomLog;

import java.util.concurrent.Callable;

@CustomLog
public abstract class Renderer {
    private final RenderingThread renderThread;

    protected Renderer() {
        logger.log("HI! Rendering will be done by ME! (" + this.getClass().getName() + ")");
        logger.log("Creating RenderingThread");
        renderThread = new RenderingThread();
        Thread.ofPlatform().name("Rendering").factory().newThread(renderThread).start();
    }

    public void queueTask(GraphTask g) {
        renderThread.queue(g);
    }

    protected void rendererSetup(EngineLoadEvent e) { queueTask(new GraphTask() {
        @Override
        public String name() {
            return "Renderer<Setup>";
        }

        @Override
        public void execute() throws InterruptedException {
            setup(e);
        }
    }); } protected abstract void setup(EngineLoadEvent e);


    protected void rendererRenderFrame(EngineRenderEvent e) { queueTask(new GraphTask() {
        @Override
        public String name() {
            return "Renderer<RenderFrame>";
        }

        @Override
        public void execute() throws InterruptedException {
            renderFrame(e);
        }
    }); } protected abstract void renderFrame(EngineRenderEvent e);

    protected void rendererCleanup() { queueTask(new GraphTask() {
        @Override
        public String name() {
            return "Renderer<Cleanup>";
        }

        @Override
        public void execute() throws InterruptedException {
            cleanup();
        }
    }); } protected abstract void cleanup();
}
