package dk.sebsa.spellbook.graphics.stages;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.LayerStack;
import dk.sebsa.spellbook.graphics.opengl.GL2D;
import dk.sebsa.spellbook.graphics.opengl.RenderStage;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;

/**
 * Renders a layerstack to the screen
 *
 * @author sebs
 * @since 1.0.0
 */
public class UIStage extends RenderStage {
    private final LayerStack stack;

    /**
     * @param window The window to render to
     * @param stack  Layerstack to render
     */
    public UIStage(GLFWWindow window, LayerStack stack) {
        super(window);
        this.stack = stack;
    }

    @Override
    public String getName() {
        return "UI";
    }

    @Override
    protected void draw(Rect r, FrameData frameData) {
        for (Layer l : stack.stack) {
            l.ensureRender(frameData.marble, window.rect);
        }
        frameData.marble.postRenderReset();
    }

    @Override
    public void renderFBO(Rect windowRect) {
        // I HATE THAT THIS EXIST
        // IF YOU NEED TO KNOW WHY THIS EXISTS
        // read the giant ass motherfucker comment at the end of RenderPipeline.render(EngineRenderEvent e)
        GL2D.drawTextureWithTextCords(fbo.material, windowRect, Rect.verticalFlippedUV);
    }

    @Override
    public void destroy() {

    }
}
