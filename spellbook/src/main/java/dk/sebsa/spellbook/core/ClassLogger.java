package dk.sebsa.spellbook.core;

import dk.sebsa.mana.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Logs to a SpellbookLogger with a classes classname
 * This should not be passed between clases
 * @since 1.0.0
 * @author sebs
 */
public class ClassLogger implements Logger {
    private final SpellbookLogger mainLogger;
    private final String className;

    public ClassLogger(Object o, Logger mainLogger) {
        this.mainLogger = (SpellbookLogger) mainLogger;
        this.className = o.getClass().getSimpleName();
    }

    @Override
    public void trace(Object... objects) {
        mainLogger.trace(className, objects);
    }

    @Override
    public void log(Object... objects) {
        mainLogger.log(className, objects);
    }

    @Override
    public void warn(Object... objects) {
        mainLogger.warn(className, objects);
    }

    @Override
    public void err(Object... objects) {
        mainLogger.err(className, objects);
    }

    @Override
    public void close() {

    }

    public String stackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
