package dk.sebsa.spellbook.marble;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineCleanupEvent;
import dk.sebsa.spellbook.core.events.EngineFirstFrameEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.graphics.opengl.GL2D;
import dk.sebsa.spellbook.graphics.opengl.SpriteSheet;
import dk.sebsa.spellbook.util.ThreeKeyHashMap;
import dk.sebsa.spellbook.util.TwoKeyHashMap;
import lombok.CustomLog;

/**
 * UI and UX module for Spellbook
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class Marble implements Module {
    private static final TwoKeyHashMap<Font, String, MarbleIMRenderer> rendererHashMap = new TwoKeyHashMap<>();
    /* FONTS STUFF */
    private static final ThreeKeyHashMap<String, Integer, Integer, Font> fontMap = new ThreeKeyHashMap<>();
    private Font defaultFont;

    @EventListener
    public void engineFirstFrame(EngineFirstFrameEvent e) {
        defaultFont = font(new Identifier("spellbook", "fonts/Inter.ttf"), 48);
    }

    /**
     * Gets the MarbleIMRender with the terms specified
     *
     * @param f           The font of the renderer or null. If null the defualt font will be used (Inter, Plain, 16)
     * @param spriteSheet Identifier of the spritesheet of the renderer, or null. If null Blackstone will be used
     * @return The renderer that matches the parameters
     */
    public MarbleIMRenderer getMarbleIM(Font f, Identifier spriteSheet) {
        Font font = f != null ? f : defaultFont;
        Identifier sht = spriteSheet != null ? spriteSheet : new Identifier("spellbook", "marble/Blackstone.sht");

        return rendererHashMap.getPut(font, sht.toString(), () ->
                new MarbleIMRenderer(this, font, (SpriteSheet) AssetManager.getAssetS(sht)));
    }

    @EventListener
    public void engineCleanup(EngineCleanupEvent e) {
        // Destroy Renderers
        for (MarbleIMRenderer r : rendererHashMap.getValues()) {
            r.destroy();
        }

        // Destroy Fonts
        Spellbook.instance.getRenderer().queue(() -> {
            for (Font f : fontMap.getValues()) {
                    f.destroy();
            }
        });
    }

    @Override
    public void cleanup() {
        logger.log("Cleanup");
    }

    @Override
    public String name() {
        return "UI<Marble>";
    }

    /**
     * Loads a font from a TTF file (or from the cache) and derives its size
     *
     * @param fontType The font asset to load from
     * @param size     The point size of the Font
     * @return the new font
     */
    public Font font(Identifier fontType, int size) {
        return fontMap.getPut(fontType.toString(), size, 0, () -> {
            Font f = new Font(fontType, size);
            Spellbook.instance.getRenderer().queue(() -> Spellbook.instance.getRenderer().generateFont(f));
            return f;
        });
    }

    /**
     * This unprepares GL2D
     */
    public void postRenderReset() {
        if (GL2D.isPrepared()) GL2D.unprepare();
    }

    /**
     * Ensures GL2D is prepared
     */
    public void ensurePrepared() {
        if (!GL2D.isPrepared()) GL2D.prepare();
    }
}
