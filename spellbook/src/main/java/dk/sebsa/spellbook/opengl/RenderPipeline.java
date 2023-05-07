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
    private final List<RenderStage> renderStages;
    private final ClassLogger logger;
    private boolean hasPrintedDebugMessage = !Spellbook.instance.DEBUG;

    /**
     * A RenderPipeline with the stages provides
     * @param renderStages The stages of this RenderPipeline
     * @param logger The logger used for debug logging
     */
    private RenderPipeline(List<RenderStage> renderStages, SpellbookLogger logger) {
        this.renderStages = renderStages;
        this.logger = new ClassLogger(this, logger);
    }

    private final Rect verticalFlippedUV = new Rect(0,0,1,1);

    /**
     * Renders all the stages to a final buffer which is rendered to the screen
     * @param e Render event
     */
    public void render(EngineRenderEvent e) {
        glClearColor(1, 1, 1, 0);

        if(!hasPrintedDebugMessage) {
            hasPrintedDebugMessage = true;
            logger.trace("Rendering Pipeline: ");

            for (RenderStage stage : renderStages) {
                logger.trace(" - " + stage.getName());
            }
        }

        FBO prevFBO = null;
        List<FBO> fbos = new ArrayList<>(renderStages.size());
        for(RenderStage stage : renderStages) {
            try {
                prevFBO = stage.render(prevFBO, e.frameData);
                fbos.add(prevFBO);
            } catch (Exception ex) {
                Spellbook.instance.error("Render stage: " + stage.getName() + ", failed to run. Error: " + ex.getMessage() + "\nStacktrace: " + logger.stackTrace(ex), false);
            }
        }

        // Render the stage to the screen
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
        for(FBO fbo : fbos) { FBO.renderFBO(fbo, e.window.rect, verticalFlippedUV); }
    }

    /**
     * Builds RenderPipelines
     * @author sebs
     * @since 1.0.0
     */
    public static class RenderPipelineBuilder {
        private List<RenderStage> renderStages = new ArrayList<>();

        /**
         * Appends a stage to the end of a pipeline
         * @param stage The stage to append
         * @return This
         */
        public RenderPipelineBuilder appendStage(RenderStage stage) { renderStages.add(stage); return this; }

        /**
         * Builds the final RenderPipeline with the stages provided.
         * @param logger The main Spellbook logger
         * @return this
         */
        public RenderPipeline build(SpellbookLogger logger) {
            RenderPipeline pipeline = new RenderPipeline(renderStages, logger);
            renderStages = new ArrayList<>();

            return pipeline;
        }
    }

    /**
     * Runs cleanup/destroy methods on all stages
     */
    public void destroy() {
        for(RenderStage stage : renderStages) {
            stage.destroy();
        }
    }
}
