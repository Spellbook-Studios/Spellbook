package dk.sebsa;

import dk.sebsa.spellbook.asset.loading.FolderAssetProvider;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.core.events.EngineCreateFirstSceneEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.debug.DebugRenderStage;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.ecs.tiles.TileGrid;
import dk.sebsa.spellbook.ecs.tiles.TileSet;
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
                .renderResolution(new Rect(0, 0, 480, 270))
                .build().addAssetProvider(new FolderAssetProvider(new File("assets/"), "marco")));
    }

    @EventListener
    public void engineBuildRenderPipeline(EngineBuildRenderPipelineEvent e) {
        Application.super.engineBuildRenderPipeline(e);
        e.builder.appendStage(new DebugRenderStage(e));
    }

    @EventListener
    public void engineCreateFirstScene(EngineCreateFirstSceneEvent e) {
        entityPlayer = new Entity(ECS.ROOT);
        SpriteRenderer sr = new SpriteRenderer(Sprites.SPRITESHEET, Sprites.brick);
        entityPlayer.addComponent(sr);
        entityPlayer.addComponent(new PlayerMovement());
        sr.scale = 0.4f;

        new Camera(entityPlayer); // Cam is automatically main cam

        Entity tiles = new Entity(ECS.ROOT);
        var tilegrid = new TileGrid(() -> TileSet.fromSpriteSheet(Sprites.SPRITESHEET), 16, 16);
        tiles.addComponent(tilegrid);

        tilegrid.setCell(0, 0, 0);
        tilegrid.setCell(1, 0, 0);
        tilegrid.setCell(0, 1, 0);
        tilegrid.setCell(0, 2, 0);
        tilegrid.setCell(0, 3, 0);
        tilegrid.setCell(0, 4, 0);
        tilegrid.setCell(1, 4, 0);


        tilegrid.setCell(0, 5, 0);
        tilegrid.setCell(0, 6, 0);
        tilegrid.setCell(0, 7, 0);
        tilegrid.setCell(0, 8, 0);
        tilegrid.setCell(0, 9, 0);
        tilegrid.setCell(0, 10, 0);
        tilegrid.setCell(0, 11, 0);
        tilegrid.setCell(0, 12, 0);
        tilegrid.setCell(0, 13, 0);
        tilegrid.setCell(0, 14, 0);
        tilegrid.setCell(0, 15, 0);
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
