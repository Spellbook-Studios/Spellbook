package dk.sebsa.spellbook.marble;

import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.*;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.SpriteSheet;
import dk.sebsa.spellbook.util.ThreeKeyHashMap;
import lombok.CustomLog;

/**
 * UI and UX module for Spellbook
 *
 * @author sebs
 * @since 0.0.1a
 */
@CustomLog
public class Marble implements Module {
    @EventListener
    public void engineFirstFrame(EngineFirstFrameEvent e) {
        defaultFont = font("Inter", 16, java.awt.Font.PLAIN);
    }

    private Font defaultFont;
    private static final ThreeKeyHashMap<Font, SpriteSheet, GLSLShaderProgram, MarbleIMRenderer> rendererHashMap = new ThreeKeyHashMap<>();

    /**
     * Gets the MarbleIMRender with the terms specified
     *
     * @param f            The font of the renderer or null. If null the defualt font will be used (Inter, Plain, 16)
     * @param spriteSheet  The spritesheet of the renderer, or null. If null Blackstone will be used
     * @param shader       The shader of the renderer, or null. If null the default GUI shader will be used
     * @return The renderer that matches the parameters
     */
    public MarbleIMRenderer getMarbleIM(Font f, SpriteSheet spriteSheet, GLSLShaderProgram shader) {
        Font font = f != null ? f : defaultFont;
        SpriteSheet sht = spriteSheet != null ? spriteSheet : (SpriteSheet) AssetManager.getAssetS("/spellbook/marble/Blackstone.sht");
        GLSLShaderProgram shd = shader != null ? shader : (GLSLShaderProgram) AssetManager.getAssetS("/spellbook/shaders/SpellbookUI.glsl");

        return rendererHashMap.getPut(font, sht, shd, () -> new MarbleIMRenderer(font, sht, shd));
    }

    @EventListener
    public void engineCleanup(EngineCleanupEvent e) {
        // Destroy Renderers
        for (MarbleIMRenderer r : rendererHashMap.getValues()) {
            r.destroy();
        }

        // Destroy Fonts
        for (Font f : fontMap.getValues()) {
            f.getTexture().destroy();
        }
    }

    @Override
    public void cleanup() {
        logger.log("Cleanup");
    }

    @Override
    public String name() {
        return "UI<Marble>";
    }

    /* FONTS STUFF */
    private static final ThreeKeyHashMap<String, Integer, Integer, Font> fontMap = new ThreeKeyHashMap<>();

    /**
     * Gets a font with the specified name
     * The size will be 12
     * The style will be plain (java.awt.Font.PLAIN)
     *
     * @param name The name of the font, if null the default loaded font will be used (must be installed)
     * @return the new current font
     */
    public Font font(String name) {
        return font(name, 12, java.awt.Font.PLAIN);
    }

    /**
     * Gets a font with the specified name and size
     * The style will be plain (java.awt.Font.PLAIN)
     *
     * @param name The name of the font (must be installed)
     * @param size the point size of the Font
     * @return the new current font
     */
    public Font font(String name, int size) {
        return font(name, size, java.awt.Font.PLAIN);
    }

    /**
     * Gets a font with the specified name and size
     *
     * @param name The name of the font (must be installed)
     * @param size The point size of the Font
     * @param type The style of the font, e.g. java.awt.Font.PLAIN (1)
     * @return the new current font
     */
    public Font font(String name, int size, int type) {
        return fontMap.getPut(name, size, type, () -> new Font(new java.awt.Font(name, type, size)));
    }
}
