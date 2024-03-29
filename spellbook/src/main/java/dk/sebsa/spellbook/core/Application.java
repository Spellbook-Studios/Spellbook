package dk.sebsa.spellbook.core;

import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.core.events.EventHandler;
import dk.sebsa.spellbook.graphics.stages.SpriteStage;

/**
 * A Spellbook Application
 * Applications are automatically registered as an eventhandler to the EventBus
 *
 * @author sebs
 * @since 1.0.0
 */
public interface Application extends EventHandler {
    /**
     * Get the name of the application
     *
     * @return The name of the application
     */
    String name();

    /**
     * Get the author of the application
     *
     * @return The author of the application
     */
    String author();

    /**
     * Get the version of the application
     *
     * @return The version of the application
     */
    String version();

    @Override
    default void engineBuildRenderPipeline(EngineBuildRenderPipelineEvent e) {
        e.builder.appendStage(new SpriteStage(e));
        e.appendUIStage();
    }
}
