package dk.sebsa.spellbook.opengl;

import dk.sebsa.spellbook.core.events.LayerStack;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;

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
    protected void draw(FBO prevFBO, Rect r) {
        drawPreviousFBO(prevFBO);
        for(int i = stack.stack.size()-1; i >= 0; i--) {
            stack.stack.get(i).render(r);
        }
    }

    @Override
    public void destroy() {

    }
}
