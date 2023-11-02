package dk.sebsa;

import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.Logger;
import dk.sebsa.mana.impl.FormatBuilder;
import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.audio.OpenALModule;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.*;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.core.threading.*;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.imgui.SpellbookImGUI;
import dk.sebsa.spellbook.marble.Marble;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.opengl.OpenGLModule;
import dk.sebsa.spellbook.phys.Newton2D;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * The mother of all Spellbook programs
 *
 * @author sebs
 * @since 1.0.0
 */
public class Spellbook {
    /**
     * The one and only instance of the Spellbook engine
     */
    public static Spellbook instance;

    // Spellbook Settings and Consts
    /**
     * The version of Spellbook as denoted in the gradle module
     */
    public static final String SPELLBOOK_VERSION = "1.0.0a-SNAPSHOT";
    /**
     * Weather the capabilities.debug is true
     */
    public final boolean DEBUG;

    // Sys info
    /**
     * The graphics card that is used by OpenGL
     */
    public static String graphicsCard = "Toaster"; // Set at runtime

    // Runtime stuff
    @Getter
    private static Logger logger;
    @Getter
    private final SpellbookCapabilities capabilities;
    @Getter
    private EventBus eventBus;
    @Getter
    private final List<Module> modules = new ArrayList<>();
    private Core moduleCore;
    private final Application application;
    @Getter
    private ITaskManager taskManager;

    /**
     * The FRAME_DATA for the current frame
     */
    public static FrameData FRAME_DATA;
    private Marble marbleModule;

    /**
     * Loads and initializes a module
     *
     * @param m Module to load
     */
    public void registerModule(Module m) {
        logger.log("Module - " + m.name());
        modules.add(m);
        m.init(eventBus);
    }

    /**
     * Create a new Spellbook instance
     *
     * @param app  Starts the application
     * @param caps The new spellbook instance uses these capabilities
     */
    public static void start(Application app, SpellbookCapabilities caps) {
        if (instance != null)
            throw new RuntimeException("Having multiple applications is not supported yet.");
        else {
            new Spellbook(app, caps);
        }
    }

    private Spellbook(Application app, SpellbookCapabilities caps) {
        application = app;
        capabilities = caps;
        DEBUG = caps.spellbookDebug;
        instance = this;

        // Logger init
        if (DEBUG) {
            try {
                LogFormatter format = new FormatBuilder().buildFromFile("/spellbook/loggers/main.xml");

                // Create logger with storage logic if needed
                if (caps.logStorageMode.equals(SpellbookCapabilities.LogStorageModes.dont))
                    logger = new SpellbookLogger(format, caps);
                else logger = new StoredLogger(format, caps);
            } catch (IOException e) {
                throw new RuntimeException("Spellbook failed to start!!! >>> Logger load IOException");
            }
        } else {
            logger = new DeLogger();
        }

        // Log important debug info
        logger.log("Running Spellbook: " + SPELLBOOK_VERSION);
        logger.log(capabilities);
        logger.log(app);
        Mana.logSystemInfo(logger);

        // The guts of Spellbook
        try {
            init(); // Init Spellbook, and setup workers (+Pre-Main Loop)
            mainLoop(); // Actual main loop
        } catch (Exception | Error e) {
            logger.warn("Main loop caught exception / error: " + e.getMessage());
        }

        // Exit
        logger.log("Get those broomsticks ready, cause whe are entering cleanup!");

        cleanup();

        logger.log("Goodbye World!");
        logger.log("(Program Lifetime: " + (Time.getTime() * 0.001f) / 60 + " minutes)");
        logger.close();
    }

    private void init() {
        Time.init(logger);
        logger.log("Started time at: " + Time.getNow());

        eventBus = new EventBus();
        eventBus.registerListeners(application);

        logger.log("Registering Modules");
        moduleCore = new Core();
        registerModule(moduleCore);

        if (capabilities.disableThreading) {
            logger.log("Skipping Threading");
            taskManager = new ITaskManager() {
                @Override
                public void handleReturn() {

                }

                @Override
                public Task run(Task task) {
                    task.run();
                    return task;
                }

                @Override
                public TaskGroup run(TaskGroup tasks) {
                    for (Task t : tasks.tasks) {
                        run(t);
                    }
                    return tasks;
                }

                @Override
                public Task runNotifyOnFinish(Task task, Consumer<Task> consumer) {
                    run(task);
                    consumer.accept(task);
                    return task;
                }

                @Override
                public void shutdown() {

                }

                @Override
                public void shutdownNow() {

                }

                @Override
                public boolean awaitFinish(long timeout, TimeUnit timeUnit) {
                    return true;
                }
            };

        } else taskManager = new SpellbookTaskManager();

        // Register Non Core Modules
        if (capabilities.ecs) registerModule(new ECS());
        if (capabilities.renderingProvider.equals(SpellbookCapabilities.Rendering.opengl))
            registerModule(new OpenGLModule());
        if (DEBUG && capabilities.debugIMGUI) registerModule(new SpellbookImGUI());
        if (capabilities.audio) registerModule(new OpenALModule());
        if (capabilities.newton2D) registerModule(new Newton2D());

        marbleModule = new Marble();
        registerModule(marbleModule);

        var tasks = DynamicTaskGroup.builder().build();

        logger.log("Engine Init Event, prepare for trouble!..");
        eventBus.engine(new EngineInitEvent(capabilities, application, tasks));
        tasks.awaitDone();

        logger.log("Open the gates, Engine Load Event");
        eventBus.engine(new EngineLoadEvent(capabilities, moduleCore.getAssetManager(), application, moduleCore, tasks));
        tasks.awaitDone();

        logger.log("Initialization done!");
    }

    private void mainLoop() {
        logger.log("Entering mainLoop()");
        eventBus.engine(new EngineFirstFrameEvent(application));

        while (!GLFW.glfwWindowShouldClose(moduleCore.getWindow().getId()) && errorCount < capabilities.spellbookShutdown && !shutdown) {
            taskManager.handleReturn();

            // Process input and prepare for frame
            FRAME_DATA = new FrameData(moduleCore.getInput(), capabilities.spriteMaxLayer, marbleModule, taskManager);
            eventBus.engine(new EngineFrameEarly(FRAME_DATA));
            Time.procsessFrame();

            // Main frame, handle events
            eventBus.engine(new EngineFrameProcess(FRAME_DATA));

            // Render
            eventBus.engine(new EngineRenderEvent(FRAME_DATA, moduleCore.getWindow()));

            // Cleanup Frame (Mostly just GLFWWindow)
            eventBus.engine(new EngineFrameDone(FRAME_DATA));
        }

        logger.log("mainLoop() finished");
    }

    private void cleanup() {
        eventBus.engine(new EngineCleanupEvent());

        logger.log("TaskManager initial shutdown");
        taskManager.shutdown();

        logger.log("Cleanup Modules");
        for (Module m : modules) {
            logger.log("Module - " + m.name());
            m.cleanup();
        }

        // Cleanup leaked assets
        moduleCore.getAssetManager().engineCleanup(logger);

        // TaskManager force shutdown
        if (capabilities.shutdownTasksAwaitMillis >= 0) {
            logger.log("Awaiting taskmanager shutdown (" + capabilities.shutdownTasksAwaitMillis + " millis)");
            if (!taskManager.awaitFinish(capabilities.shutdownTasksAwaitMillis, TimeUnit.MILLISECONDS)) {
                logger.log("... force shutdown");
                taskManager.shutdownNow();
            } else {
                logger.log("... done");
            }
        }
    }

    private boolean shutdown;

    /**
     * Count of the warning logs that has occurred in this programs lifetime
     */
    public static int warnCount;
    /**
     * Count of the error logs that has occurred in this programs lifetime
     */
    public static int errorCount;

    /**
     * Use this to report errors even when you don't have a logger
     *
     * @param error    The error message
     * @param shutdown If true the program will shut down
     */
    public void error(String error, boolean shutdown) {
        logger.err(error);
        if (shutdown) this.shutdown = true;
    }
}
