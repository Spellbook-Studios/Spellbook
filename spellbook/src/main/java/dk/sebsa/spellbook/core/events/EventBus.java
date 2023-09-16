package dk.sebsa.spellbook.core.events;

import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.core.ClassLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Handles engine events and sends notifications to subscribers
 * Also collects user events for layerstacks to then later process
 *
 * @author sebs
 * @since 1.0.0
 */
public class EventBus {
    private record Listener(Object o, Method m) {
    }

    /**
     * Queue of events that happend in user space
     * Will be disptached though layerstack
     */
    public final Queue<UserEvent> userEvents = new PriorityQueue<>();
    private final HashMap<String, List<Listener>> listeners = new HashMap<>();
    private final ClassLogger logger;

    /**
     * @param logger The main spellbook logger
     */
    public EventBus(Logger logger) {
        this.logger = new ClassLogger(this, logger);
    }

    /**
     * Register all listener methods in a objects class
     *
     * @param o The object to register listeners from
     */
    public void registerListeners(Object o) {
        logger.log("Registering listeners for: " + o.getClass().getName());
        final List<Method> methods = new ArrayList<>();
        final Class<?> klass = o.getClass();
        for (final Method method : klass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) addMethod(o, method);
        }
    }

    private void addMethod(Object o, Method m) {
        logger.trace("+" + o.getClass().getName() + "." + m.getName());
        listeners.computeIfAbsent(m.getName(), k -> new ArrayList<>()).add(new Listener(o, m));
    }

    /**
     * Adds a user event to the event queue
     *
     * @param e The event
     */
    public void user(UserEvent e) {
        userEvents.offer(e);
    }

    /**
     * Dispatches and engine event to relevant listeners
     *
     * @param e The event
     */
    public void engine(Event e) {
        final String type = e.eventType().toString();
        if (!listeners.containsKey(type)) return;
        for (Listener listener : listeners.get(type)) {
            try {
                listener.m.invoke(listener.o, e);
            } catch (IllegalAccessException ex) {
                logger.err("IllegalAccessException: Failed to invoke method: " + type + " in: " + listener.o.getClass().getName());
            } catch (InvocationTargetException ex) {
                logger.err("Method invoked exception: " + type + " in: " + listener.o.getClass().getName(), logger.stackTrace(ex));
            }
        }
    }
}
