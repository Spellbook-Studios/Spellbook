package dk.sebsa.spellbook;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.core.Core;
import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.core.events.Event;
import dk.sebsa.spellbook.core.events.Layer;
import dk.sebsa.spellbook.core.events.UserEvent;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.io.KeyReleasedEvent;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.io.KeyPressedEvent;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.util.Random;
import org.lwjgl.glfw.GLFW;

public class TestLayer extends Layer {
    private final DebugLayer debugLayer;
    private final GLFWWindow window;

    public TestLayer(EngineInitEvent e, DebugLayer debugLayer) {
        super(e.logger);
        this.debugLayer = debugLayer;
        this.window = ((Core) Spellbook.instance.getModules().get(0)).getWindow();
    }

    @Override
    protected void userEvent(UserEvent e) {
        log(e);

        if(e.eventType().equals(Event.EventType.ioKeyReleased)) {
            KeyReleasedEvent keyEvent = (KeyReleasedEvent) e;
            if(keyEvent.key == GLFW.GLFW_KEY_F2) debugLayer.enabled = !debugLayer.enabled;
        } else if(e.eventType().equals(Event.EventType.ioKeyPressed)) {
            KeyPressedEvent keyEvent = (KeyPressedEvent) e;
            if(keyEvent.key == GLFW.GLFW_KEY_F11) window.fullscreen(!window.isFullscreen());
            else if(keyEvent.key == GLFW.GLFW_KEY_H) {
                Entity entity = new Entity(ECS.ROOT);
                entity.addComponent(new SpriteRenderer(AssetManager.getAssetS("/spellbook/32.spr")));
                entity.transform.setPosition(Random.getFloat(-430, 430), Random.getFloat(-200, 200), 0);
            } else if(keyEvent.key == GLFW.GLFW_KEY_G) {
                Entity entity = new Entity(ECS.ROOT);
                SpriteRenderer spriteRenderer = new SpriteRenderer(AssetManager.getAssetS("/spellbook/32.spr"));
                entity.addComponent(spriteRenderer);
                entity.addComponent(new PlayerMovement());

                spriteRenderer.scale = 2;
                spriteRenderer.layer = 1;
            }
        }
   }

    @Override
    public void render(Rect r) {

    }
}
