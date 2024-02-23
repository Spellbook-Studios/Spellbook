package dk.sebsa;

import dk.sebsa.spellbook.asset.loading.FolderAssetProvider;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.graphics.opengl.stages.SpriteStage;
import dk.sebsa.spellbook.math.Color;

import java.io.File;

public class Marco implements Application {
    public static Marco instance;

    public static void main(String[] args) {
        instance = new Marco();

        Spellbook.start(instance, SpellbookCapabilities.builder()
                .spellbookDebug(true)
                .logDisableASCIIEscapeCharacters(false)
                .clearColor(Color.red)
                .build().addAssetProvider(new FolderAssetProvider(new File("assets/"), "marco")));
    }
    @EventListener
    public void engineCreateFirstScene(EngineCreateFirstSceneEvent e) {
        new Camera(ECS.ROOT); // Cam is automatically main cam
    }

    @EventListener
    public void engineBuildRenderPipeline(EngineBuildRenderPipelineEvent e) {
        e.builder.appendStage(new SpriteStage(e));
        e.appendUIStage();
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
