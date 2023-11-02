package dk.sebsa.spellbook.core;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.asset.loading.AssetProvider;
import dk.sebsa.spellbook.asset.loading.ClassPathAssetProvider;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.core.threading.Task;
import dk.sebsa.spellbook.io.GLFWInput;
import dk.sebsa.spellbook.io.GLFWWindow;
import lombok.CustomLog;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Core module of Spellbook
 * Always present
 * Always created first, and updated first
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class Core implements Module, EventHandler {
    @Getter
    private GLFWWindow window;
    @Getter
    private GLFWInput input;
    @Getter
    private LayerStack stack;

    @Getter
    private AssetManager assetManager;

    @EventListener
    public void engineInit(EngineInitEvent e) {
        this.window = new GLFWWindow(e.application.name(), 960, 540);
        this.input = new GLFWInput(e, window);
        this.assetManager = new AssetManager();

        e.capabilities.getAssetsProviders().add(new ClassPathAssetProvider());

        e.tasks.addTask(new Task() {
            @Override
            public String name() {
                return "BuildLayerStackTask";
            }

            @Override
            public void execute() throws InterruptedException {
                stack = ((EngineBuildLayerStackEvent) Spellbook.instance.getEventBus().engine(new EngineBuildLayerStackEvent())).builder.build();
            }
        });
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        logger.log("Core: Load asset providers");

        // Assets
        e.tasks.addTask(new Task() {
            @Override
            public String name() {
                return "LoadAssetReferencesTask";
            }

            @Override
            public void execute() throws InterruptedException {
                List<AssetReference> assets = new ArrayList<>();

                for (AssetProvider provider : e.capabilities.getAssetsProviders()) {
                    logger.log(" - Provider: " + provider.toString());
                    assets.addAll(provider.getAssets());
                }

                logger.trace("Loaded Assets: ");
                for (AssetReference asset : assets) {
                    logger.trace(" " + asset);
                }

                assetManager.registerReferences(assets);

                logger.log("Asset Providers done");
            }
        });

        // Window & Input
        window.init();
        input.addCallbacks();
    }

    @Override
    public void cleanup() {
        logger.log("Core Cleanup");
        input.cleanup();
        window.destroy();
    }

    @EventListener
    public void windowResized(GLFWWindow.WindowResizedEvent e) {
        logger.log("Window Reisezed");
    }

    @EventListener
    public void engineFrameEarly(EngineFrameEarly e) {
        window.pollEvents();
    }

    @EventListener
    public void engineFrameProcess(EngineFrameProcess e) {
        stack.handleEvents(Spellbook.instance.getEventBus().userEvents);
    }

    @EventListener
    public void engineRender(EngineRenderEvent e) {
        window.update();
    }

    @EventListener
    public void engineFrameDone(EngineFrameDone e) {
        window.endFrame();
        input.endFrame();
    }

    @Override
    public String name() {
        return "SpellbookCore";
    }
}
