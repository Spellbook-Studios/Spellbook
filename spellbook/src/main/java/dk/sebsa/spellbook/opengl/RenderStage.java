package dk.sebsa.spellbook.opengl;

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
     * Gets the name of this rendering stage
     * @return The name of this rendering stage
     */
    public abstract String getName();
    private boolean init = false;
    private FBO fbo;
    /**
     * The main window that the stage draws to
     */
    protected final GLFWWindow window;

    /**
     * @param window Primary application window
     */
    public RenderStage(GLFWWindow window) {
        this.window = window;
    }

    /**
     * Creates the FBO used for rendering
     */
    public void init() {
        updateFBO();
        init = true;
    }

    private void updateFBO() {
        if(fbo != null) fbo.destroy();
        fbo = new FBO(window.getWidth(), window.getHeight(), window);
        fbo.bindFrameBuffer();
        glEnable(GL_DEPTH_TEST);
        fbo.unBind();
    }

    /**
     * Renders the stage to it's FBO
     *
     * @param prevFBO   The fbo of the previous stage
     * @param frameData
     * @return FBO with rendered content
     */
    protected FBO render(FBO prevFBO, FrameData frameData) {
        if(!init || window.isDirty()) init();
        fbo.bindFrameBuffer();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        draw(prevFBO, window.rect, frameData);

        fbo.unBind();
        return fbo;
    }

    private final Rect verticalFlippedUV = new Rect(0,0,1,-1);

    /**
     * Draws the previous FBO the current one
     * @param prevFBO The previous FBO
     */
    protected void drawPreviousFBO(FBO prevFBO) {
        FBO.renderFBO(prevFBO, window.rect, verticalFlippedUV);
    }

    /**
     * Draw the render stage
     *
     * @param prevFBO   The previous FBO, always render this before rendering on top
     * @param r         The place to draw to
     * @param frameData
     */
    protected abstract void draw(FBO prevFBO, Rect r, FrameData frameData);

    /**
     * Cleanup after yourselves
     */
    protected abstract void destroy();
}
