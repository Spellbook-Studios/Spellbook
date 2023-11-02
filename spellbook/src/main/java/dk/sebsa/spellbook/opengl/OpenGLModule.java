package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

/**
 * Renders with OpenGL
 *
 * @author sebs
 * @since 1.0.0
 */
public class OpenGLModule implements Module {
    private final Color clearColor = Color.neonOrange;
    private RenderPipeline pipeline;
    private boolean capRender2D;

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        pipeline = ((EngineBuildRenderPipelineEvent) Spellbook.instance.getEventBus().engine(new EngineBuildRenderPipelineEvent(e.moduleCore, e.capabilities))).builder.build();
        GL2D.init(e.moduleCore.getWindow(), e.assetManager.getAsset("/spellbook/shaders/Spellbook2d.glsl"));

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
