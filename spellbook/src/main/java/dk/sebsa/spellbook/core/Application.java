package dk.sebsa.spellbook.core;

/**
 * A Spellbook Application
 * @author sebsn
 * @since 0.0.1
 */
public abstract class Application {
    public abstract String name();
    public abstract String author();
    public abstract String version();

    @Override
    public String toString() {
        return "Application{" +
                "name=" + name() +
                ", author=" + author() +
                ", version=" + version() +
                '}';
    }
}
