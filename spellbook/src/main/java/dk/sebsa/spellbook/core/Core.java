package dk.sebsa.spellbook.core;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.asset.loading.AssetLocation;
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
@Getter
@CustomLog
public class Core implements Module {
    private GLFWWindow window;
    private GLFWInput input;
    private LayerStack stack;
    private AssetManager assetManager;

    @EventListener
    public void engineInit(EngineInitEvent e) {
        this.window = new GLFWWindow(e.application.name(), 960, 540);
        this.input = new GLFWInput(e, window);
        this.assetManager = new AssetManager();

        e.capabilities.getAssetsProviders().add(new ClassPathAssetProvider("spellbook", "spellbook/"));

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
                List<AssetLocation> assets = new ArrayList<>();
                
                logger.log("AssetProviders (" + e.capabilities.getAssetsProviders().size() + "):");
                for (AssetProvider provider : e.capabilities.getAssetsProviders()) {
                    logger.log(" - Provider: " + provider.toString());
                    assets.addAll(provider.getAssets());
                }

                logger.trace("Loaded Assets: ");
                for (AssetLocation asset : assets) {
                    logger.trace(" " + asset);
                }

                assetManager.registerReferences(assets);

                logger.log("Asset Providers done");
            }
        });

        // Window & Input
        window.init();
        input.addCallbacks();

        window.setWindowIcon(new Identifier("spellbook", "spellbook.png"));
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
    public void engineFirstFrame(EngineFirstFrameEvent e) {
        input.scanGamePads();
    }

    @EventListener
    public void engineFrameEarly(EngineFrameEarly e) {
        window.pollEvents();
        input.updateGamePad();
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
        input.endFrame();
    }

    @Override
    public String name() {
        return "SpellbookCore";
    }
}
