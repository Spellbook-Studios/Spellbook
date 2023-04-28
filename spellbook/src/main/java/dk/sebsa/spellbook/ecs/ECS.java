package dk.sebsa.spellbook.ecs;

import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.*;

/**
 * Handles the execution of component updates
 * @author sebsn
 * @since 0.0.1
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
    }

    @EventListener
    public void engineRender(EngineRenderEvent e) {
        for(Component c : e.frameData.components) {
            c.render();
        }
    }

    @Override
    public String name() {
        return "ECS";
    }
}
