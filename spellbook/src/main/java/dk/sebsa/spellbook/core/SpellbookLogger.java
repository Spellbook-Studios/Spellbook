package dk.sebsa.spellbook.core;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.Logger;
import dk.sebsa.mana.impl.FormatterImpl;

import java.io.PrintStream;

/**
 * Central log collector for all Coal Loggers
 *
 * @since 0.0.1
 * @author sebs
 */
public class SpellbookLogger implements Logger {
    private static final PrintStream out = System.out;
    private final FormatterImpl formatter;
    private int warnCount, errorCount;

    public SpellbookLogger(LogFormatter formatterIn, SpellbookCapabilities caps) {
        this.formatter = (FormatterImpl) formatterIn; // This cast will always work :D
        formatter.formatTrace = formatter.formatTrace;

        if(caps.logDisableASCIIEscapeCharacters) return;
        formatter.formatTrace = "\u001B[90m " + formatter.formatTrace;
        formatter.formatLog = "\u001B[32m " + formatter.formatLog;
        formatter.formatWarn = "\u001B[33m " + formatter.formatWarn;
        formatter.formatErr = "\u001B[91m" + formatter.formatErr;
    }

    protected void print(String s) {
        out.println(s);
    }

    protected void log(String format, String className, Object... objects) {
        for(Object o : objects) {
            print(formatter.format(format, o, className));
        }

    }

    @Override
    public void trace(Object... objects) {
        log(formatter.formatTrace, "Spellbook", objects);
    }

    @Override
    public void log(Object... objects) {
        log(formatter.formatLog, "Spellbook", objects);
    }

    @Override
    public void warn(Object... objects) {
        warnCount++; log(formatter.formatWarn, "Spellbook", objects);
    }

    @Override
    public void err(Object... objects) {
        errorCount++; log(formatter.formatErr, "Spellbook", objects);
    }

    public void trace(String className, Object... objects) {
        log(formatter.formatTrace, className, objects);
    }

    public void log(String className, Object... objects) {
        log(formatter.formatLog, className, objects);
    }

    public void warn(String className, Object... objects) {
        warnCount++; log(formatter.formatWarn, className, objects);
    }

    public void err(String className, Object... objects) {
        errorCount++; log(formatter.formatErr, className, objects);
    }

    @Override
    public void close() {
        if(errorCount > 0) err("SpellbookLogger", "Program end with " + errorCount + " error(s)", "and " + warnCount + " warning(s).");
        else if(warnCount > 0) warn("SpellbookLogger", "Program end with " + warnCount + " warning(s).");
        else log(formatter.formatLog, "SpellbookLogger", "Program ended with 0 erros or warnings. :D");
    }
}
