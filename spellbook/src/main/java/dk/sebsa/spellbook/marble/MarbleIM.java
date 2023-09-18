package dk.sebsa.spellbook.marble;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.GL2D;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.Sprite;
import dk.sebsa.spellbook.opengl.SpriteSheet;
import dk.sebsa.spellbook.util.ThreeKeyHashMap;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Immediate mode rendering of GUI components using Marble
 *
 * @author sebs
 * @since 0.0.1
 */
@Getter
public class MarbleIM {
    /**
     * Font: Calibri
     * Size: 12
     * Style: Plain
     */
    private static Font defaultFont;
    @Getter
    private static Font currFont;
    private static final ThreeKeyHashMap<String, Integer, Integer, Font> fontMap = new ThreeKeyHashMap<>();

    private static boolean prepared = false;

    private static boolean checkPrepared() {
        if (!prepared) Spellbook.instance.error("MarbleIM function called without IM being prepared", false);
        return prepared;
    }

    /**
     * Initializes MarbleIM
     */
    protected static void init() {
        defaultFont = new Font(new java.awt.Font("Inter", java.awt.Font.PLAIN, 16));
        currFont = defaultFont;
        blackStoneR = AssetManager.getAssetS("/spellbook/marble/Blackstone.sht");
        spriteSheet(blackStoneR);
    }

    /**
     * Destroys any asset in use by MarbleIM
     */
    protected static void destroy() {
        unloadSpriteSheet();

        defaultFont.getTexture().destroy();
        defaultFont = null;
        currFont = null;
        shaderProgram = null;
    }

    /**
     * Must be called before any MarbleIM calls can be issued
     * This will overwrite GL2D settings so theese can be prepared at the same time
     * You must call MarbleIM.unpreapre() when you are finished using MarbleIM
     */
    public static void prepare() {
        if (prepared) return;
        GL2D.prepare(shaderProgram);
        prepared = true;
    }

    /**
     * Must be called after finished using MarbleIM methods
     * Shouldn't be called if not prepared
     */
    public static void unprepare() {
        if (!prepared) return;
        GL2D.unprepare();
        prepared = false;
    }

    /**
     * Sets the current font to the font with the specified name
     * The size will be 16
     * The style will be plain (java.awt.Font.PLAIN)
     *
     * @param name The name of the font, if null the default loaded font will be used (must be installed)
     * @return the new current font
     */
    public static Font font(String name) {
        return font(name, 12, java.awt.Font.PLAIN);
    }

    /**
     * Sets the current font to the font with the specified name and size
     * The style will be plain (java.awt.Font.PLAIN)
     *
     * @param name The name of the font (must be installed)
     * @param size the point size of the Font
     * @return the new current font
     */
    public static Font font(String name, int size) {
        return font(name, size, java.awt.Font.PLAIN);
    }

    /**
     * Sets the current font to the font with the specified name and size
     *
     * @param name The name of the font (must be installed)
     * @param size The point size of the Font
     * @param type The style of the font, e.g. java.awt.Font.PLAIN (1)
     * @return the new current font
     */
    public static Font font(String name, int size, int type) {
        if (name == null || name.isEmpty()) currFont = defaultFont;
        currFont = fontMap.getPut(name, size, type, () -> new Font(new java.awt.Font(name, type, size)));
        return currFont;
    }

    /**
     * Renders a text label, using the current font
     *
     * @param text Text to render
     * @param x    GUI Position X
     * @param y    GUI Position Y
     */
    public static void label(String text, float x, float y) {
        if (!checkPrepared()) return;

        Map<Byte, Font.Glyph> chars = currFont.getCharTable();
        byte[] c = text.getBytes(StandardCharsets.ISO_8859_1);
        float tempX = x;

        for (byte value : c) {
            Font.Glyph glyph = chars.get(value);

            GL2D.drawTextureWithTextCords(currFont.getMaterial(), new Rect(tempX, y, glyph.scale().x, glyph.scale().y), new Rect(glyph.pos().x, glyph.pos().y, glyph.size().x, glyph.size().y));

            tempX += glyph.scale().x;
        }
    }

    /**
     * Renders a sprice to the screen
     *
     * @param rect   The dimensions and position of the sprite
     * @param sprite The sprite to render
     */
    public static void sprite(Rect rect, Sprite sprite) {
        GL2D.drawSprite(rect, sprite);
    }

    private static GLSLShaderProgram shaderProgram;

    protected static void setShader(GLSLShaderProgram shader) {
        shaderProgram = shader;
    }

    // Blackstone
    private static AssetReference blackStoneR;
    private static AssetReference currentSpriteSheetR;
    private static SpriteSheet currentSpriteSheet;

    private static Sprite sprBox;
    private static Sprite sprBoxSpecialLight;
    private static Sprite sprBoxSpecialDark;
    private static Sprite sprButton;
    private static Sprite sprButtonHover;
    private static Sprite sprButtonSpecial;
    private static Sprite sprWindow;

    /**
     * Sets the current spritesheet
     *
     * @param assetReference Reference to new spritesheet, if null default "blackstone" spritesheet will be used
     */
    public static void spriteSheet(AssetReference assetReference) {
        if (currentSpriteSheet != null) {
            currentSpriteSheetR.unReference();
            currentSpriteSheet = null;
        }

        currentSpriteSheetR = assetReference;
        if (assetReference == null) currentSpriteSheetR = blackStoneR;

        currentSpriteSheet = currentSpriteSheetR.get();

        // Get sprites
        sprBox = currentSpriteSheet.spr("Box");
        sprBoxSpecialLight = currentSpriteSheet.spr("BoxSpecialLight");
        sprBoxSpecialDark = currentSpriteSheet.spr("BoxSpecialDark");
        sprButton = currentSpriteSheet.spr("Button");
        sprButtonHover = currentSpriteSheet.spr("ButtonHover");
        sprButtonSpecial = currentSpriteSheet.spr("ButtonSpecial");
        sprWindow = currentSpriteSheet.spr("Window");
    }

    private static void unloadSpriteSheet() {
        if (!blackStoneR.equals(currentSpriteSheetR)) currentSpriteSheetR.unReference();
        else blackStoneR.unReference();
        currentSpriteSheet = null;
        currentSpriteSheetR = null;
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("Box")
     *
     * @param rect The rect to draw to
     */
    public static void box(Rect rect) {
        sprite(rect, sprBox);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("BoxSpecialLight")
     *
     * @param rect The rect to draw to
     */
    public static void boxSpecialLight(Rect rect) {
        sprite(rect, sprBoxSpecialLight);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("BoxSpecialDark")
     *
     * @param rect The rect to draw to
     */
    public static void boxSpecialDark(Rect rect) {
        sprite(rect, sprBoxSpecialDark);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("Button")
     *
     * @param rect The rect to draw to
     */
    public static void button(Rect rect) {
        sprite(rect, sprButton);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonHover")
     *
     * @param rect The rect to draw to
     */
    public static void buttonHover(Rect rect) {
        sprite(rect, sprButtonHover);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonSpecial")
     *
     * @param rect The rect to draw to
     */
    public static void buttonSpecial(Rect rect) {
        sprite(rect, sprButtonSpecial);
    }
}
