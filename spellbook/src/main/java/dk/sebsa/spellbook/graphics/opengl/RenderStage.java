package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;

import static org.lwjgl.opengl.GL11.*;

/**
 * A separate stage in rendering which renders its contents to an FBO
 * The main RenderPipeline then renders this FBO to the screen
 * Note that if dealing with bigger pipeline, you should always draw the previous FBO before to the current one
 */
public abstract class RenderStage {
    /**
     * The main window that the stage draws to
     */
    protected final GLFWWindow window;
    protected FBO fbo;
    private boolean init = false;
    
    /**
     * @param window Primary application window
     */
    public RenderStage(GLFWWindow window) {
        this.window = window;
    }

    /**
     * Gets the name of this rendering stage
     *
     * @return The name of this rendering stage
     */
    public abstract String getName();

    /**
     * Creates the FBO used for rendering
     */
    public void init() {
        updateFBO();
        init = true;
    }

    private void updateFBO() {
        if (fbo != null) fbo.destroy();
        fbo = new FBO((int) window.rect.width, (int) window.rect.height, window);
        fbo.bindFrameBuffer();
        glEnable(GL_DEPTH_TEST);
        fbo.unBind();
    }

    /**
     * Renders the stage to it's FBO
     *
     * @param frameData Active FrameData
     * @return FBO with rendered content
     */
    protected FBO render(FrameData frameData) {
        if (!init || window.isDirty()) init();
        fbo.bindFrameBuffer();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        draw(Spellbook.instance.getCapabilities().renderResolution, frameData);

        fbo.unBind();
        return fbo;
    }

    /**
     * Draw the render stage
     *
     * @param r         The place to draw to
     * @param frameData Active FrameData
     */
    protected abstract void draw(Rect r, FrameData frameData);

    /**
     * Cleanup after yourselves
     */
    protected abstract void destroy();

    /**
     * Draws the FBO
     * Note: GL2D must be prepared before calling this method
     * Why does this stupid method exist you might ask???
     * This exits literraly beacuse of a bug
     * This allows a very specific stage to draw itself inverted
     * For more context read the giant ass motherfucker comment at the end of RenderPipeline.render(EngineRenderEvent e)
     *
     * @param windowRect The rect of the window drawing to
     */
    public void renderFBO(Rect windowRect) {
        GL2D.drawTextureWithTextCords(fbo.material, windowRect, Rect.UV);
    }
}
