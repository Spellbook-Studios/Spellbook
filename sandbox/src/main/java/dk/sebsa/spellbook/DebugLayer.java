package dk.sebsa.spellbook;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.audio.SoundPlayer;
import dk.sebsa.spellbook.core.SpellbookLogger;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.imgui.ImGUILayer;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.phys.components.CircleCollider2D;
import dk.sebsa.spellbook.phys.components.SpriteCollider2D;
import dk.sebsa.spellbook.util.Random;
import imgui.ImGui;

public class DebugLayer extends ImGUILayer {
    public DebugLayer(SpellbookLogger logger) {
        super(logger);
    }

    @Override
    protected void drawImGUI() {
        if (ImGui.begin("Sandbox Controls")) {
            if (ImGui.button("Spawn Block")) {
                Entity entity = new Entity(ECS.ROOT);
                SpriteRenderer sr = new SpriteRenderer(AssetManager.getAssetS("assets/32.spr"));
                entity.addComponent(sr);
                entity.addComponent(new SpriteCollider2D(sr));
                entity.transform.setPosition(Random.getFloat(-430, 430), Random.getFloat(-200, 200), 0);
            }
            if (ImGui.button("Spawn Jukebox")) {
                Entity entity = new Entity(ECS.ROOT);
                SpriteRenderer spriteRenderer = (SpriteRenderer) entity.addComponent(new SpriteRenderer(AssetManager.getAssetS("assets/32.spr")));
                entity.addComponent(new CircleCollider2D());

                SoundPlayer sound = (SoundPlayer) entity.addComponent(new SoundPlayer());
                sound.sound = AssetManager.getAssetS("assets/fantasymphony-wcb.ogg");
                sound.loop = true;

                entity.transform.setPosition(100, 100, 0);
                sound.start();
            }

            if (ImGui.button("Add Circle Collider")) {
                CircleCollider2D c = new CircleCollider2D();
                c.radius = 32;

                ECS.ROOT.searchChildren("Player").addComponent(c);
            }


            if (ImGui.button("Add Sprite Collider")) {
                Entity player = ECS.ROOT.searchChildren("Player");
                player.addComponent(new SpriteCollider2D(player.getComponent(SpriteRenderer.class)));
            }

            if (ImGui.button("Reset Colliders")) {
                Entity player = ECS.ROOT.searchChildren("Player");
                player.removeComponent(player.getComponent(CircleCollider2D.class));
                player.removeComponent(player.getComponent(SpriteCollider2D.class));
            }

            ImGui.end();
        }
    }

    @Override
    protected boolean disableDefaultWindows() {
        return false;
    }
}
