package dk.sebsa.spellbook.core;

import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.LayerStack;
import dk.sebsa.spellbook.opengl.RenderPipeline;

/**
 * A Spellbook Application
 * @author sebsn
 * @since 0.0.1
 */
public abstract class Application {
    public abstract String name();
    public abstract String author();
    public abstract String version();

    @Override
    public String toString() {
        return "Application{" +
                "name=" + name() +
                ", author=" + author() +
                ", version=" + version() +
                '}';
    }

    public abstract RenderPipeline renderingPipeline(EngineLoadEvent e);
    public abstract LayerStack layerStack(EngineInitEvent e);
}
