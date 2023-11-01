package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.opengl.RenderPipeline;
import lombok.RequiredArgsConstructor;

/**
 * The engine needs a new RenderPipeline
 *
 * @author sebs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EngineBuildRenderPipelineEvent extends Event {
    @Override
    public EventType eventType() {
        return EventType.engineBuildRenderPipeline;
    }

    /**
     * The builder, add stages and modify the pipeline as needed
     */
    public final RenderPipeline.RenderPipelineBuilder builder = new RenderPipeline.RenderPipelineBuilder();

    /**
     * The Core module
     */
    public final Core moduleCore;
    /**
     * The main spellbook logger
     */
    public final SpellbookLogger logger;
    /**
     * Spellbook capabilities
     */
    public final SpellbookCapabilities capabilities;
}
