package dk.sebsa.spellbook.marble;

import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.graphics.opengl.GL2D;
import dk.sebsa.spellbook.graphics.opengl.GLSLShaderProgram;
import dk.sebsa.spellbook.graphics.opengl.Sprite;
import dk.sebsa.spellbook.graphics.opengl.SpriteSheet;
import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Renders UI components from a set SpriteSheet, with a specific font and a specific shader
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class MarbleIMRenderer {
    @Getter
    @Setter
    private Font font;
    private final SpriteSheet spriteSheet;
    private final GLSLShaderProgram shader;

    private final Sprite sprBox;
    private final Sprite sprBoxSpecialLight;
    private final Sprite sprBoxSpecialDark;
    private final Sprite sprButton;
    private final Sprite sprButtonHover;
    private final Sprite sprButtonSpecial;
    private final Sprite sprWindow;
    private final Marble marble;

    /**
     * @param marble      Marble instance
     * @param font        Font to use
     * @param spriteSheet Spritesheet to use
     * @param shader      Shade to use
     */
    public MarbleIMRenderer(Marble marble, Font font, SpriteSheet spriteSheet, GLSLShaderProgram shader) {
        this.marble = marble;
        this.spriteSheet = spriteSheet;
        this.shader = shader;
        this.font = font;

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
        marble.ensureShader(shader);
        Map<Byte, Font.Glyph> chars = font.getCharTable();
        byte[] c = text.getBytes(StandardCharsets.ISO_8859_1);
        float tempX = x;

        for (byte value : c) {
            Font.Glyph glyph = chars.get(value);

            GL2D.drawTextureWithTextCords(font.getMaterial(), new Rect(tempX, y, glyph.scale().x, glyph.scale().y), new Rect(glyph.pos().x, glyph.pos().y, glyph.size().x, glyph.size().y));

            tempX += glyph.scale().x;
        }
    }

    /**
     * Renders a sprite to the screen
     *
     * @param rect   The dimensions and position of the sprite
     * @param sprite The sprite to render
     */
    public void sprite(Rect rect, Sprite sprite) {
        marble.ensureShader(shader);
        GL2D.drawSprite(rect, sprite);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("Box")
     *
     * @param rect The rect to draw to
     */
    public void box(Rect rect) {
        marble.ensureShader(shader);
        sprite(rect, sprBox);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("BoxSpecialLight")
     *
     * @param rect The rect to draw to
     */
    public void boxSpecialLight(Rect rect) {
        marble.ensureShader(shader);
        sprite(rect, sprBoxSpecialLight);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("BoxSpecialDark")
     *
     * @param rect The rect to draw to
     */
    public void boxSpecialDark(Rect rect) {
        marble.ensureShader(shader);
        sprite(rect, sprBoxSpecialDark);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("Button")
     *
     * @param rect The rect to draw to
     */
    public void button(Rect rect) {
        marble.ensureShader(shader);
        sprite(rect, sprButton);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonHover")
     *
     * @param rect The rect to draw to
     */
    public void buttonHover(Rect rect) {
        marble.ensureShader(shader);
        sprite(rect, sprButtonHover);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonSpecial")
     *
     * @param rect The rect to draw to
     */
    public void buttonSpecial(Rect rect) {
        marble.ensureShader(shader);
        sprite(rect, sprButtonSpecial);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonSpecial")
     *
     * @param rect The rect to draw to
     */
    public void window(Rect rect) {
        marble.ensureShader(shader);
        sprite(rect, sprWindow);
    }

    /**
     * Unreferences / Deletes any assets used by the render
     */
    public void destroy() {
        shader.unreference();
        spriteSheet.unreference();
    }

    @Override
    public String toString() {
        return "MarbleIMRenderer{" +
                "currFont=" + font +
                ", spriteSheet=" + spriteSheet +
                ", shader=" + shader +
                '}';
    }
}
