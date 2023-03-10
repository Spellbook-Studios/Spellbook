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
        this.logger = e.logger;
        this.window = new GLFWWindow(logger, e.application.name(), Color.red, 800, 640);
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        log("Core: Load asset providers");

        // Assets
        List<AssetReference> assets = new ArrayList<>();
        e.capabilities.getAssetsProviders().add(new ClassPathAssetProvider(logger));

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

        assetManager = new AssetManager(assets);

        log("Asset Providers done");

        // Window
        window.init();
    }

    @EventListener
    public void engineCleanup(EngineCleanupEvent e) {
        assetManager.engineCleanup(logger);
    }

    @Override
    public void cleanup() {
        log("Core Cleanup");
        window.destroy();
    }

    @Override
    public String name() {
        return "SpellbookCore";
    }
}
