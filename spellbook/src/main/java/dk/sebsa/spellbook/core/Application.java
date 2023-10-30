package dk.sebsa.spellbook.core;

import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.LayerStack;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.opengl.RenderPipeline;

/**
 * A Spellbook Application
 *
 * @author sebs
 * @since 1.0.0
 */
public abstract class Application {
    /**
     * Get the name of the application
     *
     * @return The name of the application
     */
    public abstract String name();

    /**
     * Get the author of the application
     *
     * @return The author of the application
     */
    public abstract String author();

    /**
     * Get the version of the application
     *
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
     *
     * @param e The engine load event
     * @return The final renderpipeline that should be used
     */
    public abstract RenderPipeline renderingPipeline(EngineLoadEvent e);

    /**
     * Assembles the layerstack to be used
     *
     * @param e The engine init event
     * @return The final layerstack that should be used
     */
    public abstract LayerStack layerStack(EngineInitEvent e);

    /**
     * Tells the program that it can safely initilize the first scene
     *
     * @param e The ECS.ROOT entity
     */
    public abstract void createInitialScene(Entity e);
}
