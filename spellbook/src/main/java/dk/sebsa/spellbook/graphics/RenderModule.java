package dk.sebsa.spellbook.graphics;

import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import lombok.CustomLog;

@CustomLog
public class RenderModule implements Module {
    private final Renderer renderer;

    public RenderModule(Renderer renderer) {
        this.renderer = renderer;
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        renderer.queue(() -> renderer.setup(e));
    }

    @EventListener
    public void engineRender(EngineRenderEvent e) {
        renderer.queue(() -> renderer.renderFrame(e));
    }

    @Override
    public void cleanup() {
        logger.log("Cleaning up renderer: " + renderer.getClass().getName());
        renderer.queue(renderer::cleanup);
    }

    @Override
    public String name() {
        return "Rendering";
    }
}
