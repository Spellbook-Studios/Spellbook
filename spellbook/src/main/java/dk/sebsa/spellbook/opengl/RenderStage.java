package dk.sebsa.spellbook.opengl;

import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;

import static org.lwjgl.opengl.GL11.*;

public abstract class RenderStage {
    public abstract String getName();
    private boolean init = false;
    private FBO fbo;
    protected final GLFWWindow window;

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

    protected FBO render(FBO prevFBO) {
        if(!init || window.isDirty()) init();
        fbo.bindFrameBuffer();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        draw(prevFBO, window.rect);

        fbo.unBind();
        return fbo;
    }

    private final Rect verticalFlippedUV = new Rect(0,0,1,-1);

    protected void drawPreviousFBO(FBO prevFBO) {
        FBO.renderFBO(prevFBO, window.rect, verticalFlippedUV);
    }

    protected abstract void draw(FBO prevFBO, Rect r);
    protected abstract void destroy();
}
