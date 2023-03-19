package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class OpenGLModule implements Module {
    private final Color clearColor = Color.red;
    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);

        TESTSTAGE = new TestStage(e.assetManager, e.moduleCore.getWindow());

        GL2D.init(e.moduleCore.getWindow(), e.assetManager.getAsset("/spellbook/shaders/Spellbook2d.glsl"));
    }

    @Override
    public void cleanup() {
        TESTSTAGE.destroy();
        GL2D.cleanup();
    }
    private static final Rect fullUV = new Rect(0,0,1,-1);

    @EventListener
    public void engineRender(EngineRenderEvent e) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer

        FBO fbo = TESTSTAGE.render(null);
        FBO.renderFBO(fbo, e.window.rect, fullUV);

        glfwSwapBuffers(e.window.getId());
    }

    private TestStage TESTSTAGE;

    @Override
    public String name() {
        return "Rendering<OpenGL>";
    }
}
