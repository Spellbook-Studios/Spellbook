package dk.sebsa;


import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.Logger;
import dk.sebsa.mana.impl.FormatBuilder;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.SpellbookLogger;
import lombok.Getter;

import java.io.IOException;

/**
 * The mother of all Spellbook programs
 * @author sebsn
 * @since 0.0.1
 */
public class Spellbook {
    public static Spellbook instance;

    // Coal Settings and Consts
    public static final String SPELLBOOK_VERSION = "0.0.1a-SNAPSHOT";
    public final boolean DEBUG;

    // Sys info
    @Getter private static String graphicsCard = "Toaster"; // Set at runtime

    // Runtime stuff
    @Getter private final Logger logger;
    @Getter private final SpellbookCapabilities capabilities;

    public static void start(Application app, SpellbookCapabilities caps) {
        if(instance != null) throw new RuntimeException("Having multiple applications is not supported yet.");
        else {
            new Spellbook(app, caps);
        }
    }

    private Spellbook(Application app, SpellbookCapabilities caps) {
        capabilities = caps;
        DEBUG = caps.coalDebug;

        // Logger init
        try {
            LogFormatter format = new FormatBuilder().buildFromFile("/coal/loggers/main.xml");
            logger = new SpellbookLogger(format);
        } catch (IOException e) { throw new RuntimeException("Spellbook failed to start!!! >>> Logger load IOException"); }
        logger.log("Spellbook logger init");
        Mana.logSystemInfo(logger);
    }
}