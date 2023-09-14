package dk.sebsa.spellbook.ecs;

import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.*;

/**
 * Handles the execution of component updates
 * @author sebsn
 * @since 1.0.0
 */
public class ECS implements Module {
    /**
     * The current root entity
     */
    public static Scene ROOT = new Scene();

    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
    }

    private Logger logger;

    @EventListener
    public void engineInit(EngineInitEvent e) {
        this.logger = new ClassLogger(this, e.logger);
    }

    @Override
    public void cleanup() {
        for(Component c : ROOT.getAllComponent()) {
            c.onDisable();
        }
    }

    @EventListener
    public void engineFrameEarly(EngineFrameEarly e) {
        e.frameData.components = ROOT.getAllComponent();
    }

    @EventListener
    public void engineFrameProcess(EngineFrameProcess e) {
        for(Component c : e.frameData.components) {
            c.update(e.frameData);
        }

        for(Component c : e.frameData.components) {
            c.lateUpdate(e.frameData);
        }

        noDirt(ROOT);
    }

    private static void noDirt(Entity e) {
        for(Entity c : e.getChildren()) {
            c.transform.cleanDirt();
            noDirt(c);
        }
    }

    @EventListener
    public void engineRender(EngineRenderEvent e) {
        for(Component c : e.frameData.components) {
            c.render();
        }
    }

    @EventListener
    public void engineFirstFrame(EngineFirstFrameEvent e) {
        e.application.createInitialScene(ROOT);
        if(Camera.activeCamera == null) logger.warn("Initial scene doesn't contain a camera!");

    }

    @Override
    public String name() {
        return "ECS";
    }
}
