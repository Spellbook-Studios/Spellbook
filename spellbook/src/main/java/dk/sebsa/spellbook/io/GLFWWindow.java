package dk.sebsa.spellbook.io;

import dk.sebsa.Spellbook;
import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import lombok.Getter;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A representation of a GLFW window with OpenGL rendering supported
 * @since 0.0.1
 * @author sebs
 */
public class GLFWWindow {
    /**
     * The id as given by OpenGl
     */
    private long id;

    // "User" vars
    private String windowTitle;
    private Color clearColor;
    private final boolean vsync = false;

    // Info vars
    @Getter private int width, height;
    @Getter private boolean minimized = false;

    /**
     * Denotes wether the window has been "modified" within the last frame
     * Modified in the sense that the window has been moved or resized.
     */
    @Getter private boolean isDirty = true;
    @Getter private boolean isFullscreen;
    private final int[] posX = new int[1]; // The position of the window on the screen
    private final int[] posY = new int[1];  // The position of the window on the screen

    public final Rect rect = new Rect(); // A rect representing the window, with pos always equal to 0,0

    private final Logger logger;
    private void log(Object... o) { logger.log(o); }

    /**
     * @param logger The coal logger
     * @param windowTitle The title of the window
     * @param clearColor The color to use for clearing the OpenGL buffer
     * @param width The starting width of the window
     * @param height The starting height of the window
     */
    public GLFWWindow(Logger logger, String windowTitle, Color clearColor, int width, int height) {
        this.logger = logger;

        this.windowTitle = windowTitle;
        this.clearColor = clearColor;
        this.width = width;
        this.height = height;
        this.isDirty = true;

        rect.set(0,0,width,height);
    }

    /**
     * Initializes the window and shows it on screen
     */
    public void init() {
        log("GLFWWindow: Init GLFW");

        // Setup GLFW error callback
        // For now this is System.err
        GLFWErrorCallback.createPrint(System.err).set();


        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
                                  // but better safe than sorry
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // OSX Support
        //glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        //glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        //glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        log("GLFWWindow: Create window: ", this);

        // Create the window
        id = glfwCreateWindow(width,height,windowTitle,NULL,NULL);
        if ( id == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        log("GLFWWindow: Setup resize callback");
        // you can read above ^^, but this is called whenever the window is resized by the user
        glfwSetFramebufferSizeCallback(id, (window, w, h) -> {
            isDirty = true;

            if(!isFullscreen) {
                this.width = w;
                this.height = h;
                rect.set(0,0,width,height);

                log("GLFWWindow: Windows resized", this);
            }
            minimized = w == 0 && h == 0;

            glViewport(0, 0, w, h);
        });

        log("GLFWWindow: Push the first frame..");
        try (MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);  // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(id, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    id,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }   // the stack frame is popped automatically

        log("GLFWWindow: Finalizing window setup");
        glfwMakeContextCurrent(id); // Makes it so glfw knows that the window we are working with is this
        glfwSwapInterval(vsync ? 1 : 0); // Sets the swapinterval according to vsync prefrences

        // Anti Aliasing support
        glfwWindowHint(GLFW_STENCIL_BITS, 4);
        glfwWindowHint(GLFW_SAMPLES, 4);

        log("GLFWWindow: Init OpenGL support");
        // Init OpenGL
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        log("GLFWWindow: Setup GL functionality");
        // Enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Show the window
        glfwShowWindow(id);

        // Get graphics card
        Spellbook.graphicsCard = glGetString(GL_RENDER) + " " + glGetString(GL_VENDOR);
        log("GLFWWindow: Graphics Card: " + Spellbook.graphicsCard);
    }

    /**
     * Tells the window to process frame events
     */
    public void update() {

    }

    /**
     * Destroys the window and the GLFW context with it
     */
    public void destroy() {
        log("GLFWWindow: Window Cleanup");
        glfwDestroyWindow(id);

        log("GLFWWindow: Terminate GLFW");
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    @Override
    public String toString() {
        return "GLFWWindow{" +
                "id=" + id +
                ", windowTitle='" + windowTitle + '\'' +
                ", clearColor=" + clearColor +
                ", vsync=" + vsync +
                ", minimized=" + minimized +
                ", isDirty=" + isDirty +
                ", isFullscreen=" + isFullscreen +
                ", rect=" + rect +
                '}';
    }
}
