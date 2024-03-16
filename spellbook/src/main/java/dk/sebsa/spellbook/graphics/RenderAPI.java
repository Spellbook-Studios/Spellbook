package dk.sebsa.spellbook.graphics;

import dk.sebsa.spellbook.asset.loading.AssetLocation;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.graphics.opengl.Texture;
import dk.sebsa.spellbook.marble.Font;
import dk.sebsa.spellbook.marble.FontType;
import lombok.CustomLog;

import java.util.concurrent.TimeUnit;

/**
 * An abstraction for the most common rendering api dependent functions
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public abstract class RenderAPI {
    private final RenderingThread renderThread;
    private final Thread thread;

    protected RenderAPI() {
        logger.log("HI! Rendering will be done by ME! (" + this.getClass().getName() + ")");
        logger.log("Creating RenderingThread");
        renderThread = new RenderingThread();
        thread = Thread.ofPlatform().name("Rendering").factory().newThread(renderThread);
        thread.start();
    }

    /**
     * Checks weather the taskqueue is empty
     *
     * @return true if there are no more tasks for the renderer to handle, false otherwise
     */
    public boolean frameDone() { return renderThread.taskQueue.isEmpty() && !renderThread.runningTask.get(); }

    /**
     * Waits until the taskqueue is empty (frameDone() returns true)
     */
    public void waitForDone() {
        while(!frameDone()) {
            frameDone();
        }
    }

    private void queue(GraphTask g) {
        if(Thread.currentThread() == thread) g.run();
        else renderThread.queue(g);
    }

    /**
     * Queues a runnable as an anonymous graphtask
     * If called from the rendering thread it will be run imminently
     *
     * @param r Runnable to run
     */
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

    /**
     * Should load the texture from the disk and make it ready to be used by this rendering API
     * @param t Texture to load
     * @param l Location of the texture
     */
    public abstract void loadTexture(Texture t, AssetLocation l);

    /**
     * Sets the texture as the active texture
     * @param t The texture to bind
     * @param textureUnit The texture unit to bind to (0-31 for OpenGL)
     */
    public abstract void bindTexture(Texture t, int textureUnit);

    /**
     * Unbinds the texture at the specified texture unit
     *
     * @param textureUnit The texture unit to clear (0-31 for OpenGL)
     */
    public abstract void unbindTexture(int textureUnit);

    /**
     * Should make the font type to ready for more fonts to be created
     *
     * @param fontType FontType to prepare
     */
    public abstract void generateFontType(FontType fontType);

    /**
     * Should generate the fonts textures and glyphs so the font is ready to be used by this renderer
     * @param f Font to generate
     */
    public abstract void generateFont(Font f);

    /**
     * Indicates to the renderer that this resource is no longer used
     * @param texture Expired resource
     */
    public abstract void destroy(Texture texture);


    protected abstract void setup(EngineLoadEvent e);
    protected abstract void renderFrame(EngineRenderEvent e);
    protected void cleanup() {
        destroy();
        renderThread.startCleanup();
    }
    protected abstract void destroy();

    /**
     * Waits for the thread to stop by itself
     *
     * @param timeout  The maximum amount of time to wait
     * @param timeUnit The unit of the previous time value
     * @return true if the thread stops by itself, false otherwise
     */
    public boolean awaitFinish(long timeout, TimeUnit timeUnit) {
        try {
            thread.join(timeUnit.toMillis(timeout));
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * Crashes the thread, used to force the thread to shut down
     */
    public void interruptThread() {
        thread.interrupt();
    }
}
