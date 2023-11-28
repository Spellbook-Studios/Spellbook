package dk.sebsa.spellbook.marble;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineCleanupEvent;
import dk.sebsa.spellbook.core.events.EngineFirstFrameEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.opengl.GL2D;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.SpriteSheet;
import dk.sebsa.spellbook.util.ThreeKeyHashMap;
import lombok.CustomLog;

/**
 * UI and UX module for Spellbook
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class Marble implements Module {
    @EventListener
    public void engineFirstFrame(EngineFirstFrameEvent e) {
        defaultFont = font("Inter", 16, java.awt.Font.PLAIN);
    }

    private Font defaultFont;
    private static final ThreeKeyHashMap<Font, String, String, MarbleIMRenderer> rendererHashMap = new ThreeKeyHashMap<>();

    /**
     * Gets the MarbleIMRender with the terms specified
     *
     * @param f           The font of the renderer or null. If null the defualt font will be used (Inter, Plain, 16)
     * @param spriteSheet Identifier of the spritesheet of the renderer, or null. If null Blackstone will be used
     * @param shader      Identifier of the shader, or null. If null the default GUI shader will be used
     * @return The renderer that matches the parameters
     */
    public MarbleIMRenderer getMarbleIM(Font f, Identifier spriteSheet, Identifier shader) {
        Font font = f != null ? f : defaultFont;
        Identifier sht = spriteSheet != null ? spriteSheet : new Identifier("spellbook", "marble/Blackstone.sht");
        Identifier shd = shader != null ? shader : new Identifier("spellbook", "shaders/SpellbookUI.glsl");

        return rendererHashMap.getPut(font, sht.toString(), shd.toString(), () ->
                new MarbleIMRenderer(this, font, (SpriteSheet) AssetManager.getAssetS(sht), (GLSLShaderProgram) AssetManager.getAssetS(shd)));
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
     * @return the font
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
     * @return the font
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
     * @return the font
     */
    public Font font(String name, int size, int type) {
        return fontMap.getPut(name, size, type, () -> new Font(new java.awt.Font(name, type, size)));
    }

    /**
     * Loads a font from a TTF file (or from the cache) and derives its size
     *
     * @param fontType The font asset to load from
     * @param size     The point size of the Font
     * @return the new font
     */
    public Font font(FontType fontType, float size) {
        String name = fontType.getLocation().location();

        return fontMap.getPut(name, (int) size, java.awt.Font.PLAIN, () -> new Font(fontType.getFont().deriveFont(size)));
    }

    private GLSLShaderProgram preparedShader = null;

    /**
     * This unprepares GL2D, unbinds the shader and should be called after rendering with MarbleIMRenderer
     */
    public void postRenderReset() {
        if (preparedShader != null) {
            GL2D.unprepare();
            preparedShader = null;
        }
    }

    /**
     * Ensures GL2D is prepared with a specific shader
     *
     * @param shader The shader to check against and bind if not already bound
     */
    public void ensureShader(GLSLShaderProgram shader) {
        if (!java.util.Objects.equals(preparedShader, shader)) preparedShader = GL2D.prepare(shader);
    }
}
