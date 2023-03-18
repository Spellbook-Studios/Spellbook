package dk.sebsa.spellbook.core;

import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.asset.loading.AssetProvider;
import dk.sebsa.spellbook.asset.loading.ClassPathAssetProvider;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Color;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Core implements Module, EventHandler {
    private Logger logger;
    @Getter private GLFWWindow window;

    private void log(Object... o) { logger.log(o); }
    @Getter private AssetManager assetManager;

    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
    }

    @EventListener
    public void engineInit(EngineInitEvent e) {
        this.logger = new ClassLogger(this, e.logger);
        this.window = new GLFWWindow(e.logger, e.application.name(), 800, 640);
        this.assetManager = new AssetManager();

        e.capabilities.getAssetsProviders().add(new ClassPathAssetProvider(e.logger));
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

        if(e.capabilities.spellbookDebug) {
            logger.trace("Loaded Assets: ");
            for(AssetReference asset : assets) {
                logger.trace(" " + asset);
            }
        }

        assetManager.registerReferences(assets);

        log("Asset Providers done");

        // Window
        window.init();
    }

    @Override
    public void cleanup() {
        log("Core Cleanup");
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
    public void engineRender(EngineRenderEvent e) {
        window.update();
    }

    @EventListener
    public void engineFrameDone(EngineFrameDone e) {
        window.endFrame();
    }

    @Override
    public String name() {
        return "SpellbookCore";
    }
}
