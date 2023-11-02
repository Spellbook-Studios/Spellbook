package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import lombok.CustomLog;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * A collection of rendering stages
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class RenderPipeline {
    private final List<RenderStage> renderStages;
    private boolean hasPrintedDebugMessage = !Spellbook.instance.DEBUG;

    /**
     * A RenderPipeline with the stages provides
     *
     * @param renderStages The stages of this RenderPipeline
     */
    private RenderPipeline(List<RenderStage> renderStages) {
        this.renderStages = renderStages;
    }

    /**
     * Renders all the stages to a final buffer which is rendered to the screen
     *
     * @param e Render event
     */
    public void render(EngineRenderEvent e) {
        glClearColor(1, 1, 1, 0);

        if (!hasPrintedDebugMessage) {
            hasPrintedDebugMessage = true;
            logger.trace("Rendering Pipeline: ");

            for (RenderStage stage : renderStages) {
                logger.trace(" - " + stage.getName());
            }
        }

        for (RenderStage stage : renderStages) {
            try {
                stage.render(e.frameData);
            } catch (Exception ex) {
                Spellbook.instance.error("Render stage: " + stage.getName() + ", failed to run. Error: " + ex.getMessage() + "\nStacktrace: " + logger.stackTrace(ex), false);
            }
        }

        // Render the stage to the screen
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        // FBO.renderFBOS(fbos, e.window.rect, Rect.UV);
        // This didn't work why???? The UIStage keeps drawing its stuff cucking inverted
        // Like IDK why, but LWJGL likes having a wierd coordinate system
        // In previous versions of this engine all fbo's were rendered upside down
        // So using Rect.verticallyFlippedUV would have work and all stages whould be rendered equally
        // But for some reason everything now works... except for UIStage
        // Instead we do this shit
        GL2D.prepare();
        for (RenderStage stage : renderStages) {
            stage.renderFBO(e.window.rect);
        }
        GL2D.unprepare();
    }

    /**
     * Builds RenderPipelines
     *
     * @author sebs
     * @since 1.0.0
     */
    public static class RenderPipelineBuilder {
        private List<RenderStage> renderStages = new ArrayList<>();

        /**
         * @return true if no renderstages are added, false otherwise
         */
        public boolean isEmpty() {
            return renderStages.isEmpty();
        }

        /**
         * Appends a stage to the end of a pipeline
         *
         * @param stage The stage to append
         * @return This
         */
        public RenderPipelineBuilder appendStage(RenderStage stage) {
            renderStages.add(stage);
            return this;
        }

        /**
         * Builds the final RenderPipeline with the stages provided.
         *
         * @return this
         */
        public RenderPipeline build() {
            RenderPipeline pipeline = new RenderPipeline(renderStages);
            renderStages = new ArrayList<>();

            return pipeline;
        }
    }

    /**
     * Runs cleanup/destroy methods on all stages
     */
    public void destroy() {
        for (RenderStage stage : renderStages) {
            stage.destroy();
        }
    }
}
