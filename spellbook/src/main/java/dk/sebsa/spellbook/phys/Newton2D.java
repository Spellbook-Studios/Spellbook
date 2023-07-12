package dk.sebsa.spellbook.phys;

import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineFrameProcess;
import dk.sebsa.spellbook.core.events.EventBus;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;
import dk.sebsa.spellbook.phys.components.CircleCollider2D;
import dk.sebsa.spellbook.phys.components.Collider2D;

/**
 * Spellbook's own 2d physics system
 * @author sebs
 * @since 1.0.0
 */
public class Newton2D implements Module {
    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
    }

    @EventListener
    public void engineFrameProcess(EngineFrameProcess e) {
        for(Collider2D mover : e.frameData.newton2DMovers) {
            for(Collider2D solid : e.frameData.newton2DSolids) {
                if(mover instanceof BoxCollider2D) solid.collides((BoxCollider2D) mover);
                else if(mover instanceof CircleCollider2D) solid.collides((CircleCollider2D) mover);
            }

            e.frameData.newton2DSolids.add(mover);
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public String name() {
        return "Phys<Newton2D>";
    }
}
