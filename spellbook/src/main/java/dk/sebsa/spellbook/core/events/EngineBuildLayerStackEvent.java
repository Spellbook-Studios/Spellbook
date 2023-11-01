package dk.sebsa.spellbook.core.events;

import dk.sebsa.spellbook.core.SpellbookLogger;
import lombok.RequiredArgsConstructor;

/**
 * The engine needs a new LayerStack
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineBuildLayerStackEvent extends Event {
    @Override
    public EventType eventType() {
        return EventType.engineBuildLayerStack;
    }

    /**
     * The main spellbook logger
     */
    public final SpellbookLogger logger;
    
    /**
     * The builder, add stacks and modify the builder during the event
     */
    public final LayerStack.LayerStackBuilder builder = new LayerStack.LayerStackBuilder();
}
