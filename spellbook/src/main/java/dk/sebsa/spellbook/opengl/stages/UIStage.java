package dk.sebsa.spellbook.opengl.stages;

import dk.sebsa.spellbook.FrameData;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.LayerStack;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.FBO;
import dk.sebsa.spellbook.opengl.RenderStage;

public class UIStage extends RenderStage {
    private final LayerStack stack;
    public UIStage(GLFWWindow window, LayerStack stack) {
        super(window);
        this.stack = stack;
    }

    @Override
    public String getName() {
        return "UIStage";
    }

    @Override
    protected void draw(FBO prevFBO, Rect r, FrameData frameData) {
        drawPreviousFBO(prevFBO);
        for(int i = stack.stack.size()-1; i >= 0; i--) {
            Layer l = stack.stack.get(i);
            l.ensureRender(r);
        }
    }

    @Override
    public void destroy() {

    }
}
