package dk.sebsa.spellbook.core.events;

import lombok.Getter;

/**
 * Represents an event that the user caused, and that should be handled by UI layers
 * @since 1.0.0
 * @author sebs
 */
public abstract class UserEvent extends Event implements Comparable<UserEvent> {
    @Getter private boolean blocked = false;
    private static int i = 0;
    private final int id = i++;

    /**
     * Indicates to the layerstack that this event has been handled
     */
    public void block() {
        blocked = true;
    }

    @Override
    public int compareTo(UserEvent o) {
        return id-o.id;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + eventType() +
                ", id=" + id +
                '}';
    }
}
