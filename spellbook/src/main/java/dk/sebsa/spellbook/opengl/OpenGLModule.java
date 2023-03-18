package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.math.Color;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class OpenGLModule implements Module {
    private Color clearColor = Color.red;
    private AssetReference shaderR;
    private GLSLShaderProgram shader;
    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);

        shaderR = e.assetManager.getAsset("/spellbook/shaders/test.glsl");
        shader = shaderR.get();
    }

    @Override
    public void cleanup() {
        shader = null;
        shaderR.unRefrence();
    }

    @EventListener
    public void engineRender(EngineRenderEvent e) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer



        glfwSwapBuffers(e.window.getId());
    }

    @Override
    public String name() {
        return "Rendering<OpenGL>";
    }
}
