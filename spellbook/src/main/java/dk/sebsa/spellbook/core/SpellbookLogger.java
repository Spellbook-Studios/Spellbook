package dk.sebsa.spellbook.core;

import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.impl.PrintStreamLogger;

/**
 * Logger used by the engine components
 * @since 0.0.1
 * @author sebs
 */
public class SpellbookLogger extends PrintStreamLogger {
    public SpellbookLogger(LogFormatter formatter) {
        super(System.out, formatter);
    }
}
