package dk.sebsa;

import dk.sebsa.spellbook.asset.loading.FolderAssetProvider;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.events.EngineCreateFirstSceneEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;

import java.io.File;

public class Marco implements Application {
    public static Marco instance;
    public Entity entityPlayer;

    public static void main(String[] args) {
        instance = new Marco();

        Spellbook.start(instance, SpellbookCapabilities.builder()
                .spellbookDebug(true)
                .logDisableASCIIEscapeCharacters(false)
                .clearColor(Color.red)
                .renderResolution(new Rect(0,0,480, 270))
                .build().addAssetProvider(new FolderAssetProvider(new File("assets/"), "marco")));
    }
    @EventListener
    public void engineCreateFirstScene(EngineCreateFirstSceneEvent e) {
        new Camera(ECS.ROOT); // Cam is automatically main cam

        entityPlayer = new Entity(ECS.ROOT);
        entityPlayer.addComponent(new SpriteRenderer(Sprites.SPRITESHEET, Sprites.brick));
    }

    @Override
    public String name() {
        return "Marco";
    }

    @Override
    public String author() {
        return "Sebsa";
    }

    @Override
    public String version() {
        return "1";
    }
}
