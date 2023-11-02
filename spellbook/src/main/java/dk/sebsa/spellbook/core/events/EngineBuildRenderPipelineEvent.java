package dk.sebsa.spellbook.core.events;

import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.opengl.RenderPipeline;
import dk.sebsa.spellbook.opengl.stages.UIStage;
import lombok.RequiredArgsConstructor;

/**
 * The engine needs a new RenderPipeline
 * If nothing is added to the builder the module will add a SpriteStage and UI stage for you
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
     * Spellbook capabilities
     */
    public final SpellbookCapabilities capabilities;

    /**
     * adds a UIStage to the builder
     * Shortcut for writing:
     * builder.appendStage(new UIStage(event.moduleCore.getWindow(), event.moduleCore.getStack()))
     */
    public void appendUIStage() {
        builder.appendStage(new UIStage(moduleCore.getWindow(), moduleCore.getStack()));
    }
}
