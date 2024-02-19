package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.graphics.opengl.stages.SpriteStage;
import dk.sebsa.spellbook.graphics.opengl.stages.UIStage;
import dk.sebsa.spellbook.graphics.Renderer;
import lombok.CustomLog;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

/**
 * Renders with OpenGL
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class OpenGLRenderer extends Renderer {
    private Color clearColor;
    private RenderPipeline pipeline;
    private boolean capRender2D;

    @Override
    public void setup(EngineLoadEvent e) {
        logger.log("Init OpenGL support");
        // Init OpenGL
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        glfwMakeContextCurrent(e.moduleCore.getWindow().getId());
        GL.createCapabilities();

        logger.log("Setup GL functionality");
        // Enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Get graphics card
        Spellbook.graphicsCard = glGetString(GL_RENDER) + " " + glGetString(GL_VENDOR);
        logger.log("Graphics Card: " + Spellbook.graphicsCard);

        clearColor = e.capabilities.clearColor;

        // BuildRenderPipeline
        var event = new EngineBuildRenderPipelineEvent(e.moduleCore, e.capabilities);
        var builder = ((EngineBuildRenderPipelineEvent) Spellbook.instance.getEventBus().engine(event)).builder;

        // If the builder is empty it will add some default stuff
        if (builder.isEmpty())
            builder.appendStage(new SpriteStage(event)).appendStage(new UIStage(e.moduleCore.getWindow(), e.moduleCore.getStack()));
        pipeline = builder.build();

        // Init GL2D and Sprite2D
        GL2D.init(e.moduleCore.getWindow(), (GLSLShaderProgram) e.assetManager.getAsset(new Identifier("spellbook", "shaders/Spellbook2d.glsl")));

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
    @Override
    public void renderFrame(EngineRenderEvent e) {
        glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer

        pipeline.render(e);

        glfwSwapBuffers(e.window.getId());
    }
}
