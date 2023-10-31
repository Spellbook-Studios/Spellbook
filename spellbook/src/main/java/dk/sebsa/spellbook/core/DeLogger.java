package dk.sebsa.spellbook.core;

import dk.sebsa.Spellbook;
import dk.sebsa.SpellbookCapabilities;

/**
 * An empty logger used when debug mode is disabled
 *
 * @author sebs
 * @since 1.0.0
 */
public class DeLogger extends SpellbookLogger {
    /**
     * Empty Delogger what'd you expect
     */
    public DeLogger() {
        super(null, SpellbookCapabilities.builder().logDisableASCIIEscapeCharacters(true).build());
    }

    @Override
    public void trace(Object... objects) {

    }

    @Override
    public void log(Object... objects) {

    }

    @Override
    public void warn(Object... objects) {

    }

    @Override
    public void err(Object... objects) {
        Spellbook.errorCount++;
    }

    @Override
    public void close() {

    }

    @Override
    protected void print(String s) {

    }

    @Override
    protected void log(String format, String className, Object... objects) {

    }

    @Override
    public void trace(String className, Object... objects) {

    }

    @Override
    public void log(String className, Object... objects) {

    }

    @Override
    public void warn(String className, Object... objects) {

    }

    @Override
    public void err(String className, Object... objects) {

    }
}
