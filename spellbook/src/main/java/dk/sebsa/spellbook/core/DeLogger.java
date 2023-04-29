package dk.sebsa.spellbook.core;

import dk.sebsa.mana.Logger;

/**
 * An empty logger used when debug mode is disabled
 * @author sebs
 * @since 1.0.0
 */
public class DeLogger implements Logger {
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

    }

    @Override
    public void close() {

    }
}
