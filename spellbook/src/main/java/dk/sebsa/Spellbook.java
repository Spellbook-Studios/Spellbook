package dk.sebsa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.Logger;
import dk.sebsa.mana.impl.FormatBuilder;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.asset.TextAsset;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.core.events.EngineCleanupEvent;
import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.math.Time;
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
    @Getter
    private static String graphicsCard = "Toaster"; // Set at runtime

    // Runtime stuff
    @Getter private static Logger logger;
    @Getter
    private final SpellbookCapabilities capabilities;
    @Getter
    private EventBus eventBus;
    @Getter
    private final List<Module> modules = new ArrayList<>();
    private Core moduleCore;

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
        capabilities = caps;
        DEBUG = caps.spellbookDebug;

        // Logger init
        try {
            LogFormatter format = new FormatBuilder().buildFromFile("/spellbook/loggers/main.xml");
            logger = new SpellbookLogger(format);
        } catch (IOException e) {
            throw new RuntimeException("Spellbook failed to start!!! >>> Logger load IOException");
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

        logger.log("Engine Init Event, prepare for trouble!..");
        eventBus.engine(new EngineInitEvent(logger, capabilities));

        logger.log("Open the gates, Engine Load Event");
        eventBus.engine(new EngineLoadEvent(capabilities));

        logger.log("Initialization done!");
    }

    private void mainLoop() {
        logger.log("Entering mainLoop()");

        logger.log("mainLoop() finished");
    }

    private void cleanup() {
        eventBus.engine(new EngineCleanupEvent());

        logger.log("Cleanup Modules");
        for (Module m : modules) {
            logger.log("Module - " + m.name());
            m.cleanup();
        }
    }
    public void requestShutdown() {

    }
}
