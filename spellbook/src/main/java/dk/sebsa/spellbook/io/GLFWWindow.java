package dk.sebsa.spellbook.io;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.math.Vector2f;
import lombok.CustomLog;
import lombok.Getter;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A representation of a GLFW window with OpenGL rendering supported
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class GLFWWindow {
    /**
     * A rect representing the window, within GLFW window coordinate space
     */
    public final Rect winRect = new Rect();
    /**
     * A rect representing the windows framebuffer
     */
    public final Rect rect = new Rect();
    private final boolean vsync = false;

    private final int wWidth;
    private final int wHeight;
    private final int[] posX = new int[1]; // The position of the window on the screen
    private final int[] posY = new int[1]; // The position of the window on the screen
    /**
     * The id as given by OpenGl
     */
    @Getter
    private long id;
    // "User" vars
    private String windowTitle;
    @Getter
    private boolean minimized = false;
    @Getter
    private Vector2f frameBufferScale;
    /**
     * Denotes weather the window has been "modified" within the last frame
     * Modified in the sense that the window has been moved or resized.
     */
    @Getter
    private boolean isDirty = true;
    @Getter
    private boolean isFullscreen; // Can be set by the user but is first set on the next frame
    private boolean actuallyFullscreen; // Weather the window is currently in fullscreen
    private float oldW, oldH;

    /**
     * @param windowTitle The title of the window
     * @param width       The starting width of the window
     * @param height      The starting height of the window
     */
    public GLFWWindow(String windowTitle, int width, int height) {
        this.windowTitle = windowTitle;
        this.wWidth = width;
        this.wHeight = height;
    }

    /**
     * Initializes the window and shows it on screen
     */
    public void init() {
        logger.log("Init GLFW");

        // Setup GLFW error callback
        glfwSetErrorCallback(((error, description) -> {
            Spellbook.instance.error("GLFW Error [" + Integer.toHexString(error) + "]: " + GLFWErrorCallback.getDescription(description), false);
        }));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        // but better safe than sorry
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // OSX Support
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        logger.log("Create window: ", this);

        // Create the window
        id = glfwCreateWindow(wWidth, wHeight, windowTitle, NULL, NULL);
        if (id == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get FrameBuffer size
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            glfwGetFramebufferSize(this.id, pWidth, pHeight);
            rect.set(0, 0, pWidth.get(0), pHeight.get(0));
        } frameBufferScale = calculateWindowFramebufferScaling();

        logger.log("Window rect", rect);
        logger.log("Window Framebuffer to Window scale = " + frameBufferScale);

        logger.log("Setup resize callback");
        // you can read above ^^, but this is called whenever the window is resized by
        // the user
        glfwSetFramebufferSizeCallback(id, (window, w, h) -> {
            isDirty = true;

            setFramebufferAndWindowSize(); // This is done so we both have the window and the framebuffer size.

            logger.log("Windows resized", this);
            Spellbook.instance.getEventBus().engine(new WindowResizedEvent(rect));

            minimized = w == 0 && h == 0;
        });

        logger.log("Center window");
        setFramebufferAndWindowSize();
        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center the window
        if (vidmode != null) {
            glfwSetWindowPos(
                    id,
                    (int) ((vidmode.width() - winRect.width) / 2),
                    (int) (vidmode.height() - winRect.height) / 2);
        } else {
            logger.warn("glfwGetVideoMode returned null");
        }

        logger.log("Finalizing window setup");
        glfwMakeContextCurrent(id); // Makes it so glfw knows that the window we are working with is this
        glfwSwapInterval(vsync ? 1 : 0); // Sets the swapinterval according to vsync prefrences

        // Anti Aliasing support
        glfwWindowHint(GLFW_STENCIL_BITS, 4);
        glfwWindowHint(GLFW_SAMPLES, 4);

        // Show the window
        glfwShowWindow(id);
    }

    /**
     * Tells the window to process frame events
     */
    public void update() {
        if (isFullscreen != actuallyFullscreen) {
            actuallyFullscreen = isFullscreen;
            logger.log("Changed fullscreen to: " + isFullscreen);

            isDirty = true;
            if (isFullscreen) {
                oldW = rect.width;
                oldH = rect.height;
                GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                if (videoMode == null) {
                    logger.err("glfwGetVideoMode returned null");
                } else {
                    glfwGetWindowPos(id, posX, posY);
                    glfwSetWindowMonitor(id, glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(),
                            GLFW_DONT_CARE);
                    // Enable v-sync
                    glfwSwapInterval(vsync ? 1 : 0);
                }
            } else
                glfwSetWindowMonitor(id, 0, posX[0], posY[0], (int) oldW, (int) oldH, GLFW_DONT_CARE);
        }
    }

    protected Vector2f calculateWindowFramebufferScaling() {
        setFramebufferAndWindowSize();
        return new Vector2f(rect.width / winRect.width, rect.height / winRect.height);
    }

    protected void setFramebufferAndWindowSize() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            IntBuffer pFWidth = stack.mallocInt(1); // int*
            IntBuffer pFHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(id, pWidth, pHeight);
            glfwGetFramebufferSize(id, pFWidth, pFHeight);
            rect.set(0,0,pFWidth.get(0), pFHeight.get(0)); winRect.set(0,0,pWidth.get(0),pHeight.get(0));
        }
    }

    /**
     * Tells the window that the frame is done as resets isDirty
     */
    public void endFrame() {
        isDirty = false;
    }

    /**
     * Destroys the window and the GLFW context with it
     */
    @SuppressWarnings("DataFlowIssue")
    public void destroy() {
        logger.log("Window Cleanup");
        glfwSetFramebufferSizeCallback(id, null).free();
        glfwDestroyWindow(id);

        logger.log("Terminate GLFW");
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    @Override
    public String toString() {
        return "GLFWWindow{" +
                "id=" + id +
                ", windowTitle='" + windowTitle + '\'' +
                ", vsync=" + vsync +
                ", minimized=" + minimized +
                ", isDirty=" + isDirty +
                ", isFullscreen=" + isFullscreen +
                ", rect=" + rect +
                '}';
    }

    /**
     * Polls input events
     */
    public void pollEvents() {
        glfwPollEvents();
    }

    /**
     * Sets the current fullscreen Status
     * If set the window will reflect the changes on the next frame
     *
     * @param fullscreen The new fullscreen status
     */
    public void fullscreen(boolean fullscreen) {
        this.isFullscreen = fullscreen;
    }

    /**
     * Changes the window title
     *
     * @param title The new title
     */
    public void setWindowTitle(String title) {
        glfwSetWindowTitle(id, title);
        windowTitle = title;
    }

    /**
     * Event thrown if the window is resized in anyway
     */
    public static class WindowResizedEvent extends Event {
        /**
         * A rect representing the resized window
         */
        public final Rect window;

        /**
         * Event caused when the user or the program resizes the window
         *
         * @param window The new rect representing the window
         */
        public WindowResizedEvent(Rect window) {
            this.window = window;
        }

        @Override
        public EventType eventType() {
            return EventType.windowResized;
        }
    }
}
