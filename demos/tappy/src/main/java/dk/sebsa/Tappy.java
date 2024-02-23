package dk.sebsa;

import dk.sebsa.components.BGManager;
import dk.sebsa.components.ObstacleComponent;
import dk.sebsa.components.PipesManager;
import dk.sebsa.components.TappyController;
import dk.sebsa.layers.DeathScreen;
import dk.sebsa.layers.MainMenuScreen;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.asset.loading.FolderAssetProvider;
import dk.sebsa.spellbook.audio.SoundListener;
import dk.sebsa.spellbook.audio.SoundPlayer;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.ecs.Camera;
import dk.sebsa.spellbook.ecs.ECS;
import dk.sebsa.spellbook.ecs.Entity;
import dk.sebsa.spellbook.graphics.opengl.components.SpriteRenderer;
import dk.sebsa.spellbook.graphics.opengl.stages.SpriteStage;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Time;
import dk.sebsa.spellbook.phys.components.BoxCollider2D;

import java.io.File;

public class Tappy implements Application {
    public static Tappy instance;
    private Entity tappy;
    private Layer mainMenuScreen, deathScreen;
    private Entity pipes;

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
        Time.timeScale = 0;
        new Camera(ECS.ROOT); // Cam is automatically main cam and doesnt need to move

        // Add Tappy
        this.tappy = new Entity(ECS.ROOT);
        SpriteRenderer sp = new SpriteRenderer(new Identifier("tappy", "tappy.spr"));
        tappy.addComponent(sp);
        tappy.addComponent(new SoundListener());
        tappy.addComponent(new SoundPlayer(new Identifier("tappy", "jump.ogg")));
        sp.scale = 3f;
        tappy.tag = "player";

        // Add floor
        var floor = new Entity(ECS.ROOT);
        floor.transform.setPosition(0, -265, 0);
        var collider = new ObstacleComponent();
        collider.size.set(960, 48);
        floor.addComponent(collider);
        floor.addComponent(new SoundPlayer(new Identifier("tappy", "death.ogg")));

        // Add celling
        var celling = new Entity(ECS.ROOT);
        celling.transform.setPosition(0, 290, 0);
        var cellingCollider = new BoxCollider2D();
        cellingCollider.size.set(960, 48);
        celling.addComponent(cellingCollider);

        // Background
        var bg = new Entity(ECS.ROOT);
        bg.addComponent(new BGManager());
    }

    @EventListener
    public void engineBuildRenderPipeline(EngineBuildRenderPipelineEvent e) {
        e.builder.appendStage(new SpriteStage(e));
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
        tappy.getComponent(SoundPlayer.class).start();
        tappy.transform.setPosition(0, -20, 0);
        mainMenuScreen.enabled = false;
        deathScreen.enabled = false;

        if (pipes != null) pipes.delete();
        pipes = new Entity(ECS.ROOT);
        pipes.addComponent(new PipesManager());
        Time.timeScale = 1;
    }

    public void death() {
        tappy.removeComponent(tappy.getComponent(TappyController.class));
        deathScreen.enabled = true;
        Time.timeScale = 0;
    }
}
