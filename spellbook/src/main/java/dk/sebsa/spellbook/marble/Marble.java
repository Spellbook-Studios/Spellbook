package dk.sebsa.spellbook.marble;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineCleanupEvent;
import dk.sebsa.spellbook.core.events.EngineFirstFrameEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.graphics.opengl.GL2D;
import dk.sebsa.spellbook.graphics.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.graphics.opengl.SpriteSheet;
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
        defaultFont = font(new Identifier("spellbook", "fonts/Inter.ttf"), 48);
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

    /* FONTS STUFF */
    private static final ThreeKeyHashMap<String, Integer, Integer, Font> fontMap = new ThreeKeyHashMap<>();

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
