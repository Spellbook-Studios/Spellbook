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
    /**
     * Get the name of the application
     * @return The name of the application
     */
    public abstract String name();
    /**
     * Get the author of the application
     * @return The author of the application
     */
    public abstract String author();
    /**
     * Get the version of the application
     * @return The version of the application
     */
    public abstract String version();

    @Override
    public String toString() {
        return "Application{" +
                "name=" + name() +
                ", author=" + author() +
                ", version=" + version() +
                '}';
    }

    /**
     * Assembles the renderPipeline to be used
     * @return The final renderpipeline that should be used
     */
    public abstract RenderPipeline renderingPipeline(EngineLoadEvent e);
    /**
     * Assembles the layerstack to be used
     * @return The final layerstack that should be used
     */
    public abstract LayerStack layerStack(EngineInitEvent e);
}
