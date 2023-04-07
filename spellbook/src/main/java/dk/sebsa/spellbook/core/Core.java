package dk.sebsa.spellbook.core;

import dk.sebsa.Spellbook;
import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.asset.loading.AssetProvider;
import dk.sebsa.spellbook.asset.loading.ClassPathAssetProvider;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.io.GLFWInput;
import dk.sebsa.spellbook.io.GLFWWindow;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Core implements Module, EventHandler {
    private Logger logger;
    @Getter private GLFWWindow window;
    @Getter private GLFWInput input;
    @Getter private LayerStack stack;

    private void log(Object... o) { logger.log(o); }
    @Getter private AssetManager assetManager;

    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
    }

    @EventListener
    public void engineInit(EngineInitEvent e) {
        this.logger = new ClassLogger(this, e.logger);
        this.window = new GLFWWindow(e.logger, e.application.name(), 960, 540);
        this.input = new GLFWInput(e, window);
        this.assetManager = new AssetManager();

        e.capabilities.getAssetsProviders().add(new ClassPathAssetProvider(e.logger));

        this.stack = e.application.layerStack(e);
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        log("Core: Load asset providers");

        // Assets
        List<AssetReference> assets = new ArrayList<>();

        for(AssetProvider provider : e.capabilities.getAssetsProviders()) {
            log(" - Provider: " + provider.toString());
            assets.addAll(provider.getAssets());
        }

        logger.trace("Loaded Assets: ");
        for(AssetReference asset : assets) {
            logger.trace(" " + asset);
        }

        assetManager.registerReferences(assets);

        log("Asset Providers done");

        // Window & Input
        window.init();
        input.addCallbacks();
    }

    @Override
    public void cleanup() {
        log("Core Cleanup");
        input.cleanup();
        window.destroy();
    }

    @EventListener
    public void windowResized(GLFWWindow.WindowResizedEvent e) {
        log("Window Reisezed");
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
