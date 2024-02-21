package dk.sebsa.spellbook.graphics;

import dk.sebsa.spellbook.asset.loading.AssetLocation;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.graphics.opengl.Texture;
import dk.sebsa.spellbook.marble.Font;
import dk.sebsa.spellbook.marble.FontType;
import lombok.CustomLog;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@CustomLog
public abstract class Renderer {
    private final RenderingThread renderThread;
    private final Thread thread;

    protected Renderer() {
        logger.log("HI! Rendering will be done by ME! (" + this.getClass().getName() + ")");
        logger.log("Creating RenderingThread");
        renderThread = new RenderingThread();
        thread = Thread.ofPlatform().name("Rendering").factory().newThread(renderThread);
        thread.start();
    }

    public boolean frameDone() { return renderThread.taskQueue.isEmpty(); }
    public void waitForDone() {
        while(!frameDone()) {
            frameDone();
        }
    }

    private void queue(GraphTask g) {
        if(Thread.currentThread() == thread) g.run();
        else renderThread.queue(g);
    }
    public void queue(Runnable r) {
        queue(new GraphTask() {
            @Override
            public String name() {
                return "Renderer<Anon>";
            }

            @Override
            public void execute() throws InterruptedException {
                r.run();
            }
        });
    }

    public abstract void loadTexture(Texture t, AssetLocation l);
    public abstract void bindTexture(Texture t, int textureUnit);
    public abstract void unbindTexture(int textureUnit);

    public abstract void generateFontType(FontType fontType);
    public abstract void generateFont(Font f);

    public abstract void destroy(Texture texture);


    protected abstract void setup(EngineLoadEvent e);
    protected abstract void renderFrame(EngineRenderEvent e);
    protected void cleanup() {
        destroy();
        renderThread.startCleanup();
    }
    protected abstract void destroy();

    public boolean awaitFinish(long timeout, TimeUnit timeUnit) {
        try {
            thread.join(timeUnit.toMillis(timeout));
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void interruptThread() {
        thread.interrupt();
    }
}
