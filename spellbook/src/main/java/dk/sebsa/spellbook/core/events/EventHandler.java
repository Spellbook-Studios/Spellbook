package dk.sebsa.spellbook.core.events;

public interface EventHandler {
    public default void engineInit(Event e) {};
}
