package dk.sebsa.spellbook.core;

import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.impl.PrintStreamLogger;

import java.io.PrintStream;

public class SpellbookLogger extends PrintStreamLogger {
    public SpellbookLogger(LogFormatter formatter) {
        super(System.out, formatter);
    }
}
