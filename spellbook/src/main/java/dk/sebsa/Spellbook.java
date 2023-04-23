package dk.sebsa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.Logger;
import dk.sebsa.mana.impl.FormatBuilder;
import dk.sebsa.spellbook.audio.OpenALModule;
import dk.sebsa.spellbook.core.*;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.imgui.SpellbookImGUI;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.opengl.OpenGLModule;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;

/**
 * The mother of all Spellbook programs
 * 
 * @author sebsn
 * @since 0.0.1
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
    public static final String SPELLBOOK_VERSION = "0.0.1a-SNAPSHOT";
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
    @Getter private static Logger logger;
    @Getter
    private final SpellbookCapabilities capabilities;
    @Getter
    private EventBus eventBus;
    @Getter
    private final List<Module> modules = new ArrayList<>();
    private Core moduleCore;
    private final Application application;

    /**
     * Loads and initializes a module
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
        if(DEBUG) {
            try {
                LogFormatter format = new FormatBuilder().buildFromFile("/spellbook/loggers/main.xml");

                // Create logger with storage logic if needed
                if(caps.logStorageMode.equals(SpellbookCapabilities.LogStorageModes.dont)) logger = new SpellbookLogger(format, caps);
                else logger = new StoredLogger(format, caps);
            } catch (IOException e) {
                throw new RuntimeException("Spellbook failed to start!!! >>> Logger load IOException");
            }
        } else {
            logger = new DeLogger();
        }

        // Log important debug info
        logger.log("Running Spellbook: " + SPELLBOOK_VERSION);
        logger.log(app);
        Mana.logSystemInfo(logger);

        // The guts of Spellbook
        try {
            init(); // Init Spellbook, and setup workers (+Pre-Main Loop)
            mainLoop(); // Actual main loop
        } catch (Exception | Error e) {
            e.printStackTrace();
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

        logger.log("Creating EventBus");
        eventBus = new EventBus(logger);

        logger.log("Registering Modules");
        moduleCore = new Core();
        registerModule(moduleCore);

        // Add Non Core Modules
        if(capabilities.renderingProvider.equals(SpellbookCapabilities.Rendering.opengl)) registerModule(new OpenGLModule());
        if(DEBUG && capabilities.debugIMGUI) registerModule(new SpellbookImGUI());
        if(capabilities.audio) registerModule(new OpenALModule());

        logger.log("Engine Init Event, prepare for trouble!..");
        eventBus.engine(new EngineInitEvent((SpellbookLogger) logger, capabilities, application));

        logger.log("Open the gates, Engine Load Event");
        eventBus.engine(new EngineLoadEvent(capabilities, moduleCore.getAssetManager(), application, moduleCore, (SpellbookLogger) logger));

        logger.log("Initialization done!");
    }

    private void mainLoop() {
        logger.log("Entering mainLoop()");

        while (!GLFW.glfwWindowShouldClose(moduleCore.getWindow().getId())) {
            // Process input and prepare for frame
            eventBus.engine(new EngineFrameEarly());
            Time.procsessFrame();

            // Main frame, handle events
            eventBus.engine(new EngineFrameProcess());

            // Render
            eventBus.engine(new EngineRenderEvent(moduleCore.getWindow()));

            // Cleanup Frame (Mostly just GLFWWindow)
            eventBus.engine(new EngineFrameDone());
        }

        logger.log("mainLoop() finished");
    }

    private void cleanup() {
        eventBus.engine(new EngineCleanupEvent());

        logger.log("Cleanup Modules");
        for (Module m : modules) {
            logger.log("Module - " + m.name());
            m.cleanup();
        }

        // Cleanup leaked assets
        moduleCore.getAssetManager().engineCleanup(logger);
    }

    /**
     * Use this to report errors even when you don't have a logger
     * @param error The error message
     * @param shutdown If true the program will shut down
     */
    public void error(String error, boolean shutdown) {
        logger.err(error);
    }
}
