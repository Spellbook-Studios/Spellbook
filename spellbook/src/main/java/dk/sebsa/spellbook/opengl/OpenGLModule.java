package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.stages.SpriteStage;
import dk.sebsa.spellbook.opengl.stages.UIStage;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

/**
 * Renders with OpenGL
 *
 * @author sebs
 * @since 1.0.0
 */
public class OpenGLModule implements Module {
    private Color clearColor;
    private RenderPipeline pipeline;
    private boolean capRender2D;

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        clearColor = e.capabilities.clearColor;
        var event = new EngineBuildRenderPipelineEvent(e.moduleCore, e.capabilities);
        var builder = ((EngineBuildRenderPipelineEvent) Spellbook.instance.getEventBus().engine(event)).builder;

        // If the builder is empty it will add some default stuff
        if (builder.isEmpty())
            builder.appendStage(new SpriteStage(event)).appendStage(new UIStage(e.moduleCore.getWindow(), e.moduleCore.getStack()));
        pipeline = builder.build();

        GL2D.init(e.moduleCore.getWindow(), (GLSLShaderProgram) e.assetManager.getAsset("/spellbook/shaders/Spellbook2d.glsl"));

        capRender2D = e.capabilities.render2D;
        if (capRender2D) {
            Sprite2D.init(e);
        }
    }

    @Override
    public void cleanup() {
        GL2D.cleanup();
        pipeline.destroy();

        if (capRender2D) {
            Sprite2D.destroy();
        }
    }

    private static final Rect invertedUV = new Rect(0, 0, 1, -1);

    @EventListener
    public void engineRender(EngineRenderEvent e) {
        glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer

        pipeline.render(e);

        glfwSwapBuffers(e.window.getId());
    }

    @Override
    public String name() {
        return "Rendering<OpenGL>";
    }
}
