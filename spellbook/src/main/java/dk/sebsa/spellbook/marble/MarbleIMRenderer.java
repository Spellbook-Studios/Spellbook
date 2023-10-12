package dk.sebsa.spellbook.marble;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.opengl.GL2D;
import dk.sebsa.spellbook.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.opengl.Sprite;
import dk.sebsa.spellbook.opengl.SpriteSheet;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Renders components
 *
 * @author sebs
 * @since 1.0.0
 */
public class MarbleIMRenderer {
    private static boolean prepared = false;
    private final Font currFont;
    private final AssetReference spriteSheetR;
    private final SpriteSheet spriteSheet;
    private final AssetReference shaderR;
    private final GLSLShaderProgram shader;

    private static Sprite sprBox;
    private static Sprite sprBoxSpecialLight;
    private static Sprite sprBoxSpecialDark;
    private static Sprite sprButton;
    private static Sprite sprButtonHover;
    private static Sprite sprButtonSpecial;
    private static Sprite sprWindow;

    private boolean checkPrepared() {
        if (!prepared) Spellbook.instance.error("MarbleIM function called without IM being prepared", false);
        return prepared;
    }


    /**
     * Must be called before any MarbleIM calls can be issued
     * This will overwrite GL2D settings so theese can be prepared at the same time
     * You must call MarbleIM.unpreapre() when you are finished using MarbleIM
     */
    public void prepare() {
        if (prepared) return;
        GL2D.prepare(shader);
        prepared = true;
    }

    /**
     * Must be called after finished using MarbleIM methods
     * Shouldn't be called if not prepared
     */
    public void unprepare() {
        if (!prepared) return;
        GL2D.unprepare();
        prepared = false;
    }

    /**
     * @param font         Font to use
     * @param spriteSheetR Spritesheet to use
     * @param shaderR      Shade to use
     */
    public MarbleIMRenderer(Font font, AssetReference spriteSheetR, AssetReference shaderR) {
        this.spriteSheetR = spriteSheetR;
        this.shaderR = shaderR;
        this.currFont = font;
        shader = shaderR.get();

        spriteSheet = spriteSheetR.get();

        // Get sprites
        sprBox = spriteSheet.spr("Box");
        sprBoxSpecialLight = spriteSheet.spr("BoxSpecialLight");
        sprBoxSpecialDark = spriteSheet.spr("BoxSpecialDark");
        sprButton = spriteSheet.spr("Button");
        sprButtonHover = spriteSheet.spr("ButtonHover");
        sprButtonSpecial = spriteSheet.spr("ButtonSpecial");
        sprWindow = spriteSheet.spr("Window");
    }

    /**
     * Renders a text label, using the current font
     *
     * @param text Text to render
     * @param x    GUI Position X
     * @param y    GUI Position Y
     */
    public void label(String text, float x, float y) {
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
    public void sprite(Rect rect, Sprite sprite) {
        GL2D.drawSprite(rect, sprite);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("Box")
     *
     * @param rect The rect to draw to
     */
    public void box(Rect rect) {
        sprite(rect, sprBox);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("BoxSpecialLight")
     *
     * @param rect The rect to draw to
     */
    public void boxSpecialLight(Rect rect) {
        sprite(rect, sprBoxSpecialLight);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("BoxSpecialDark")
     *
     * @param rect The rect to draw to
     */
    public void boxSpecialDark(Rect rect) {
        sprite(rect, sprBoxSpecialDark);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("Button")
     *
     * @param rect The rect to draw to
     */
    public void button(Rect rect) {
        sprite(rect, sprButton);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonHover")
     *
     * @param rect The rect to draw to
     */
    public void buttonHover(Rect rect) {
        sprite(rect, sprButtonHover);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonSpecial")
     *
     * @param rect The rect to draw to
     */
    public void buttonSpecial(Rect rect) {
        sprite(rect, sprButtonSpecial);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonSpecial")
     *
     * @param rect The rect to draw to
     */
    public void window(Rect rect) {
        sprite(rect, sprWindow);
    }

    /**
     * Unreferences / Deletes any assets used by the render
     */
    public void destroy() {
        shaderR.unReference();
        spriteSheetR.unReference();
    }

    @Override
    public String toString() {
        return "MarbleIMRenderer{" +
                "currFont=" + currFont +
                ", spriteSheet=" + spriteSheet +
                ", shader=" + shader +
                '}';
    }
}
