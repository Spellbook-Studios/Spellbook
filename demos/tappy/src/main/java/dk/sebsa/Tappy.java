package dk.sebsa;

import dk.sebsa.components.ObstacleComponent;
import dk.sebsa.components.TappyController;
import dk.sebsa.layers.DeathScreen;
import dk.sebsa.layers.MainMenuScreen;
import dk.sebsa.spellbook.asset.loading.FolderAssetProvider;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.debug.DebugRenderStage;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.opengl.stages.SpriteStage;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;

import java.io.File;

public class Tappy implements Application {
    public static Tappy instance;
    private Entity tappy;
    private Layer mainMenuScreen, deathScreen;

    public static void main(String[] args) {
        instance = new Tappy();

        Spellbook.start(instance, SpellbookCapabilities.builder()
                .spellbookDebug(true)
                .logDisableASCIIEscapeCharacters(false)
                .clearColor(Color.azure)
                .build().addAssetProvider(new FolderAssetProvider(new File("assets/"), "tappy")));
    }

    @EventListener
    public void engineCreateFirstScene(EngineCreateFirstSceneEvent e) {
        var cam = new Camera(ECS.ROOT);

        // Add Tappy
        this.tappy = new Entity(ECS.ROOT);
        SpriteRenderer sp = new SpriteRenderer("tappy/tappy.spr");
        tappy.addComponent(sp);
        sp.scale = 3f;

        // Add floor
        var floor = new Entity(ECS.ROOT);
        floor.transform.setPosition(0, -250, 0);
        var collider = new ObstacleComponent();
        collider.size.set(960, 48);
        floor.addComponent(collider);

        // Add celling
        var celling = new Entity(ECS.ROOT);
        celling.transform.setPosition(0, 290, 0);
        var cellingCollider = new BoxCollider2D();
        cellingCollider.size.set(960, 48);
        celling.addComponent(cellingCollider);
    }

    @EventListener
    public void engineBuildRenderPipeline(EngineBuildRenderPipelineEvent e) {
        e.builder.appendStage(new SpriteStage(e));
        e.builder.appendStage(new DebugRenderStage(e));
        e.appendUIStage();
    }

    @EventListener
    public void engineBuildLayerStack(EngineBuildLayerStackEvent e) {
        mainMenuScreen = new MainMenuScreen();
        deathScreen = new DeathScreen();
        deathScreen.enabled = false;

        e.builder.appendLayer(mainMenuScreen)
                .appendLayer(deathScreen);
    }

    @Override
    public String name() {
        return "TappyBird";
    }

    @Override
    public String author() {
        return "Sebsa";
    }

    @Override
    public String version() {
        return "1";
    }

    public void start() {
        tappy.addComponent(new TappyController(tappy.getComponent(SpriteRenderer.class)));
        tappy.transform.setPosition(0, -20, 0);
        mainMenuScreen.enabled = false;
        deathScreen.enabled = false;
    }

    public void death() {
        tappy.removeComponent(tappy.getComponent(TappyController.class));
        deathScreen.enabled = true;
    }
}
