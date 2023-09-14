package dk.sebsa.spellbook.opengl.stages;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.LayerStack;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.GL2D;
import dk.sebsa.spellbook.opengl.RenderStage;

/**
 * Renders a layerstack to the screen
 * @since 1.0.0
 * @author sebs
 */
public class UIStage extends RenderStage {
    private final LayerStack stack;

    /**
     * @param window The window to render to
     * @param stack Layerstack to render
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
        for(int i = stack.stack.size()-1; i >= 0; i--) {
            Layer l = stack.stack.get(i);
            l.ensureRender(r);
        }
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
