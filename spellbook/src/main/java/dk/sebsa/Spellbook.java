package dk.sebsa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.Logger;
import dk.sebsa.mana.impl.FormatBuilder;
import dk.sebsa.spellbook.core.*;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.opengl.OpenGLModule;
import lombok.Getter;

/**
 * The mother of all Spellbook programs
 * 
 * @author sebsn
 * @since 0.0.1
 */
public class Spellbook {
    public static Spellbook instance;

    // Spellbook Settings and Consts
    public static final String SPELLBOOK_VERSION = "0.0.1a-SNAPSHOT";
    public final boolean DEBUG;

    // Sys info
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
        if(capabilities.renderingProvider.equals(SpellbookCapabilities.Rendering.opengl)) registerModule(new OpenGLModule());

        logger.log("Engine Init Event, prepare for trouble!..");
        eventBus.engine(new EngineInitEvent(logger, capabilities, application));

        logger.log("Open the gates, Engine Load Event");
        eventBus.engine(new EngineLoadEvent(capabilities, moduleCore.getAssetManager(), application, moduleCore, (SpellbookLogger) logger));

        logger.log("Initialization done!");
    }

    private void mainLoop() {
        logger.log("Entering mainLoop()");

        while(Time.getTime() < 5*1000) {
            // Process input and prepare for frame
            eventBus.engine(new EngineFrameEarly());
            Time.procsessFrame();

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
    public void error(String error, boolean shutdown) {
        logger.err(error);
    }
}
