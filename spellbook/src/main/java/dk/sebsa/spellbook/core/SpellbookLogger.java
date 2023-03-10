package dk.sebsa.spellbook.core;

import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.impl.PrintStreamLogger;

public class SpellbookLogger extends PrintStreamLogger {
    public SpellbookLogger(LogFormatter formatter) {
        super(System.out, formatter);
    }
}
