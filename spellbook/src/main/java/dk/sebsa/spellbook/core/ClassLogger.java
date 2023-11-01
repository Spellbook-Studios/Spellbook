package dk.sebsa.spellbook.core;

import dk.sebsa.Spellbook;
import dk.sebsa.mana.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Logs to a SpellbookLogger with a classes classname
 * Also provides some utility functions
 * Anotate a class with @CustomLog to autoamtically get a logger object
 *
 * @author sebs
 * @since 1.0.0
 */
public class ClassLogger implements Logger {
    private final SpellbookLogger mainLogger;
    private final String className;

    /**
     * @param clazz      Then class this logger belongs to
     * @param mainLogger The main spellbook logger
     */
    public ClassLogger(Class<?> clazz, Logger mainLogger) {
        this.mainLogger = (SpellbookLogger) mainLogger;
        this.className = clazz.getSimpleName();
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


    /**
     * Creates a new classlogger using the main spellbook logger
     *
     * @param clazz The class this belongs this
     * @return The new logger
     */
    public static ClassLogger getLogger(Class<?> clazz) {
        return new ClassLogger(clazz, Spellbook.getLogger());
    }

    /**
     * Turns an exception into a string
     *
     * @param e The exception
     * @return The stacktrace of the exception
     */
    public String stackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
