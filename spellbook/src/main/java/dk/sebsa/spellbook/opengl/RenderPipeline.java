package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.math.Rect;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * A collection of rendering stages
 * @author sebs
 * @since 1.0.0
 */
public class RenderPipeline {
    private final List<RenderStage> stages;
    private final ClassLogger logger;
    private boolean hasPrintedDebugMessage = !Spellbook.instance.DEBUG;

    /**
     * A RenderPipeline with the stages provides
     * @param stages The stages of this RenderPipeline
     * @param logger The logger used for debug logging
     */
    private RenderPipeline(List<RenderStage> stages, SpellbookLogger logger) {
        this.stages = stages;
        this.logger = new ClassLogger(this, logger);
    }

    private final Rect verticalFlippedUV = new Rect(0,0,1,-1);

    /**
     * Renders all the stages to a final buffer which is rendered to the screen
     * @param e Render event
     */
    public void render(EngineRenderEvent e) {
        if(!hasPrintedDebugMessage) {
            hasPrintedDebugMessage = true;
            logger.trace("Rendering Pipeline: ");

            for (RenderStage stage : stages) {
                logger.trace(" - " + stage.getName());
            }
        }

        FBO prevFBO = null;
        for(RenderStage stage : stages) {
            try {
                prevFBO = stage.render(prevFBO, e.frameData);
            } catch (Exception ex) {
                Spellbook.instance.error("Render stage: " + stage.getName() + ", failed to run. Error: " + ex.getMessage() + "\nStacktrace: " + logger.stackTrace(ex), false);
            }
        }

        // Render the final FBO
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
        FBO.renderFBO(prevFBO, e.window.rect, verticalFlippedUV);
    }

    /**
     * Builds RenderPipelines
     * @author sebs
     * @since 1.0.0
     */
    public static class RenderPipelineBuilder {
        private List<RenderStage> stages = new ArrayList<>();

        /**
         * Appends a stage to the end of a pipeline
         * @param stage The stage to append
         * @return This
         */
        public RenderPipelineBuilder appendStage(RenderStage stage) { stages.add(stage); return this; }

        /**
         * Builds the final RenderPipeline with the stages provided.
         * @param logger The main Spellbook logger
         * @return this
         */
        public RenderPipeline build(SpellbookLogger logger) {
            RenderPipeline pipeline = new RenderPipeline(stages, logger);
            stages = new ArrayList<>();

            return pipeline;
        }
    }

    /**
     * Runs cleanup/destroy methods on all stages
     */
    public void destroy() {
        for(RenderStage stage : stages) {
            stage.destroy();
        }
    }
}
