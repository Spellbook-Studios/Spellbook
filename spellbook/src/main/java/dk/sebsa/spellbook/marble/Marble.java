package dk.sebsa.spellbook.marble;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.*;

/**
 * UI and UX module for Spellbook
 * @author sebs
 * @since 0.0.1a
 */
public class Marble implements Module {
    private ClassLogger logger;

    @Override
    public void init(EventBus eventBus) {
        eventBus.registerListeners(this);
    }

    @EventListener
    public void engineInit(EngineInitEvent e) {
        logger = new ClassLogger(this, e.logger);
    }

    @EventListener
    public void engineLoad(EngineLoadEvent e) {
        MarbleIM.init();
    }

    private AssetReference guiShaderR;
    @EventListener
    public void engineFirstFrame(EngineFirstFrameEvent e) {
        guiShaderR = AssetManager.getAssetS("/spellbook/shaders/SpellbookUI.glsl");
        MarbleIM.setShader(guiShaderR.get());
    }

    @EventListener
    public void engineCleanup(EngineCleanupEvent e) {
        MarbleIM.destroy();
        guiShaderR.unRefrence();
    }

    @Override
    public void cleanup() {

    }

    @Override
    public String name() {
        return "UI<Marble>";
    }
}
