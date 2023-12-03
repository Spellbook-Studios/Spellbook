package dk.sebsa.spellbook.core;

import dk.sebsa.Spellbook;
import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.mana.LogFormatter;
import dk.sebsa.mana.Logger;
import dk.sebsa.mana.impl.FormatterImpl;

import java.io.PrintStream;

/**
 * Central log collector for all Coal Loggers
 *
 * @author sebs
 * @since 1.0.0
 */
public class SpellbookLogger implements Logger {
    private static final PrintStream out = System.out;
    private final FormatterImpl formatter;

    /**
     * @param formatterIn The format to use
     * @param caps        Main spellbook capabilities
     */
    public SpellbookLogger(LogFormatter formatterIn, SpellbookCapabilities caps) {
        this.formatter = (FormatterImpl) formatterIn; // This cast will always work :D
    }

    protected void print(String s) {
        out.println(s);
    }

    protected void log(String format, String className, Object... objects) {
        for (Object o : objects) {
            print(formatter.format(format, o, className));
        }

    }

    /**
     * Logs a trace message
     *
     * @param objects Objects to log
     */
    @Override
    public void trace(Object... objects) {
        log(formatter.formatTrace, "Spellbook", objects);
    }

    /**
     * Logs a message
     *
     * @param objects Objects to log
     */
    @Override
    public void log(Object... objects) {
        log(formatter.formatLog, "Spellbook", objects);
    }

    /**
     * Logs a warning
     *
     * @param objects Objects to log
     */
    @Override
    public void warn(Object... objects) {
        Spellbook.warnCount++;
        log(formatter.formatWarn, "Spellbook", objects);
    }

    /**
     * Logs an error
     *
     * @param objects Objects to log
     */
    @Override
    public void err(Object... objects) {
        Spellbook.errorCount++;
        log(formatter.formatErr, "Spellbook", objects);
    }

    /**
     * Logs a trace message
     *
     * @param className Simple Name of class logging this message
     * @param objects   Objects to log
     */
    public void trace(String className, Object... objects) {
        log(formatter.formatTrace, className, objects);
    }

    /**
     * Logs a message
     *
     * @param className Simple Name of class logging this message
     * @param objects   Objects to log
     */
    public void log(String className, Object... objects) {
        log(formatter.formatLog, className, objects);
    }

    /**
     * Logs a warning
     *
     * @param className Simple Name of class logging this warning
     * @param objects   Objects to log
     */
    public void warn(String className, Object... objects) {
        Spellbook.warnCount++;
        log(formatter.formatWarn, className, objects);
    }

    /**
     * Logs an error
     *
     * @param className Simple Name of class logging this error
     * @param objects   Objects to log
     */
    public void err(String className, Object... objects) {
        Spellbook.errorCount++;
        log(formatter.formatErr, className, objects);
    }

    @Override
    public void close() {
        if (Spellbook.errorCount > 0)
            err("SpellbookLogger", "Program end with " + Spellbook.errorCount + " error(s)", "and " + Spellbook.warnCount + " warning(s).");
        else if (Spellbook.warnCount > 0)
            warn("SpellbookLogger", "Program end with " + Spellbook.warnCount + " warning(s).");
        else log(formatter.formatLog, "SpellbookLogger", "Program ended with 0 erros or warnings. :D");
    }
}
