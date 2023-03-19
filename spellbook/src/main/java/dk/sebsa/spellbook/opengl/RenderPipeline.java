package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * A collection of rendering stages
 * @author sebs
 * @since 0.0.1
 */
public class RenderPipeline {
    private final List<RenderStage> stages;
    private final ClassLogger logger;
    private boolean hasPrintedDebugMessage = !Spellbook.instance.DEBUG;

    private RenderPipeline(List<RenderStage> stages, SpellbookLogger logger) {
        this.stages = stages;
        this.logger = new ClassLogger(this, logger);
    }

    private final Rect normalRect = new Rect(0,0,1,1);

    public void render(GLFWWindow window) {
        if(!hasPrintedDebugMessage) {
            hasPrintedDebugMessage = true;
            logger.trace("Rendering Pipeline: ", getClass().getSimpleName());
            for (RenderStage stage : stages) {
                logger.trace(" - " + stage.getName(), getClass().getSimpleName());
            }
        }

        FBO prevFBO = null;
        for(RenderStage stage : stages) {
            try {
                prevFBO = stage.render(prevFBO);
            } catch (Exception e) {
                Spellbook.instance.error("Render stage: " + stage.getName() + ", failed to run. Error: " + e.getMessage() + "\nStacktrace: " + logger.stackTrace(e), false);
            }
        }

        // Render the final FBO
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
        FBO.renderFBO(prevFBO, window.rect, normalRect);
    }

    public static class RenderPipelineBuilder {
        private List<RenderStage> stages = new ArrayList<>();

        public RenderPipelineBuilder appendStage(RenderStage stage) { stages.add(stage); return this; }

        public RenderPipeline build(SpellbookLogger logger) {
            RenderPipeline pipeline = new RenderPipeline(stages, logger);
            stages = new ArrayList<>();

            return pipeline;
        }
    }
}
