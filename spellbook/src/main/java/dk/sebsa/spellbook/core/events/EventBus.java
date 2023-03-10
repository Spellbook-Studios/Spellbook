package dk.sebsa.spellbook.core.events;

import dk.sebsa.mana.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventBus {
    private final Queue<Event> userEvents = new PriorityQueue<>();
    private final HashMap<Object, HashMap<String, Method>> listeners = new HashMap<>();
    private final Logger logger;

    public EventBus(Logger logger) {
        this.logger = logger;
    }

    /**
     * Dispatches all generated user events to the layerstack
     */
    public void dispatchEvents() {

    }

    public void registerListeners(Object o) {
        logger.log("Registering listeners for: " + o.getClass().getName());
        final List<Method> methods = new ArrayList<>();
        final Class<?> klass = o.getClass();
        for(final Method method : klass.getDeclaredMethods()) {
            if(method.isAnnotationPresent(EventListener.class)) methods.add(method);
        }

        // Final Map
        final HashMap<String, Method> methodMap = new HashMap<>();
        for (Method method : methods) {
            methodMap.put(method.getName(), method);
        }

        listeners.put(o, methodMap);
    }

    public void unregister(Object o) {
        listeners.remove(o);
    }

    /**
     * Adds a user event to the event queues
     * @param e The event
     */
    public void user(Event e) {
        userEvents.add(e);
    }

    /**
     * Dispatches and engine event to relevant listeners
     * @param e The event
     */
    public void engine(Event e) {
        final String type = e.eventType().toString();
        for(Object o : listeners.keySet()) {
            final Map<String, Method> map = listeners.get(o); if(!map.containsKey(type)) continue;
            try { map.get(type).invoke(o, e); } catch (IllegalAccessException | InvocationTargetException ex) {
                logger.err("EventBus.engine(e): Failed to invoke method: " + type + " in: " + o.getClass().getName());
            }
        }
    }
}
