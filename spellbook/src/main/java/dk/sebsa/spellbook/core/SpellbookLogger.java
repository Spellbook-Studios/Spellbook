package dk.sebsa.spellbook.core;

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

    public SpellbookLogger(LogFormatter formatterIn) {
        this.formatter = (FormatterImpl) formatterIn; // This cast will always work :D
        formatter.formatTrace = formatter.formatTrace;

        formatter.formatTrace = "\u001B[90m " + formatter.formatTrace;
        formatter.formatLog = "\u001B[32m " + formatter.formatLog;
        formatter.formatWarn = "\u001B[33m " + formatter.formatWarn;
        formatter.formatErr = "\u001B[91m" + formatter.formatErr;
    }

    private void print(String s) {
        out.println(s);
    }

    private void log(String format, String className, Object... objects) {
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
        log(formatter.formatWarn, "Spellbook", objects);
    }

    @Override
    public void err(Object... objects) {
        log(formatter.formatErr, "Spellbook", objects);
    }

    public void trace(String className, Object... objects) {
        log(formatter.formatTrace, className, objects);
    }

    public void log(String className, Object... objects) {
        log(formatter.formatLog, className, objects);
    }

    public void warn(String className, Object... objects) {
        log(formatter.formatWarn, className, objects);
    }

    public void err(String className, Object... objects) {
        log(formatter.formatErr, className, objects);
    }

    @Override
    public void close() {

    }
}
