/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package dk.sebsa.spellbook;

import dk.sebsa.Spellbook;
import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.asset.loading.FolderAssetProvider;
import dk.sebsa.spellbook.audio.SoundListener;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.events.EngineBuildLayerStackEvent;
import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.core.events.EngineCreateFirstSceneEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.debug.DebugRenderStage;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.io.GamePad;
import dk.sebsa.spellbook.io.events.GamePadConnectedEvent;
import dk.sebsa.spellbook.io.events.GamePadDisConnectedEvent;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.graphics.opengl.stages.SpriteStage;

import java.io.File;

public class Sandbox implements Application {
    public DebugLayer debugLayer;
    private static boolean isDebug = false;

    public static void main(String[] args) {
        // Parse args
        for (String arg : args)
            if (arg.equalsIgnoreCase("-debug")) {
                isDebug = true;
                break;
            }

        // Create spellbook
        if (isDebug) {
            Spellbook.start(new Sandbox(), SpellbookCapabilities.builder()
                    .spellbookDebug(true)
                    .logStoreTarget("logs/latest.log")
                    .logDisableASCIIEscapeCharacters(false)
                    .debugIMGUI(true)
                    .build()
                    .addAssetProvider(new FolderAssetProvider(new File("assets/"), "sandbox"))
            );
            return;
        }

        Spellbook.start(new Sandbox(), SpellbookCapabilities.builder()
                .spellbookDebug(false)
                .logStoreTarget("logs/latest.log")
                .build()
                .addAssetProvider(new FolderAssetProvider(new File("assets/"), "sandbox"))
        );
    }

    @Override
    public String name() {
        return "Sandbox";
    }

    @Override
    public String author() {
        return "Sebsa";
    }

    @Override
    public String version() {
        return "1.0b";
    }


    @EventListener
    public void engineBuildRenderPipeline(EngineBuildRenderPipelineEvent e) {
        e.builder.appendStage(new SpriteStage(e));
        e.appendUIStage();

        if (isDebug) e.builder.appendStage(new DebugRenderStage(e));
    }

    @EventListener
    public void engineBuildLayerStack(EngineBuildLayerStackEvent e) {
        debugLayer = new DebugLayer(this);

        e.builder.appendLayer(new TestLayer(debugLayer))
                .appendLayer(debugLayer);
    }

    public static Entity player;

    @EventListener
    public void engineCreateFirstScene(EngineCreateFirstSceneEvent e) {
        player = new Camera(e.ROOT);
        player.name = "Player";
        SpriteRenderer spriteRenderer = new SpriteRenderer(new Identifier("sandbox", "32.spr"));

        spriteRenderer.scale = 200;
        spriteRenderer.layer = 1;

        player.addComponent(spriteRenderer);
        player.addComponent(new PlayerMovement());
        player.addComponent(new SoundListener());
    }

    public static GamePad gamePad;

    @EventListener
    public void gamePadConnected(GamePadConnectedEvent e) {
        if (gamePad == null)
            gamePad = e.gamePad;
    }

    @EventListener
    public void gamePadDisConnected(GamePadDisConnectedEvent e) {
        if (e.gamePad.id == gamePad.id)
            gamePad = null;
    }
}
