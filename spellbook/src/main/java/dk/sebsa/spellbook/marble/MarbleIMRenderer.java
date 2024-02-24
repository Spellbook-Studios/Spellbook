package dk.sebsa.spellbook.marble;

import dk.sebsa.spellbook.graphics.opengl.GL2D;
import dk.sebsa.spellbook.graphics.opengl.Sprite;
import dk.sebsa.spellbook.graphics.opengl.SpriteSheet;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;

/**
 * Renders UI components from a set SpriteSheet, with a specific font and a specific shader
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class MarbleIMRenderer {
    private final SpriteSheet spriteSheet;
    private final Sprite sprBox;
    private final Sprite sprBoxSpecialLight;
    private final Sprite sprBoxSpecialDark;
    private final Sprite sprButton;
    private final Sprite sprButtonHover;
    private final Sprite sprButtonSpecial;
    private final Sprite sprWindow;
    private final Marble marble;
    private final Rect rr = new Rect();
    @Getter
    @Setter
    private Font font;

    /**
     * @param marble      Marble instance
     * @param font        Font to use
     * @param spriteSheet Spritesheet to use
     */
    public MarbleIMRenderer(Marble marble, Font font, SpriteSheet spriteSheet) {
        this.marble = marble;
        this.spriteSheet = spriteSheet;
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
     * @param r    Rect to render within
     */
    public void label(String text, Rect r) {
        marble.ensurePrepared();
        GL2D.drawText(text, Color.white, font, r);
    }

    /**
     * Renders a text label, using the current font, where the text is centered horizontally
     *
     * @param text Text to render
     * @param r    Rect to render within
     */
    public void labelCenterH(String text, Rect r) {
        GL2D.drawText(text, Color.white, font, rr.set((r.width - getStringWidth(text)) * 0.5f, r.y, r.width, r.height));
    }

    /**
     * Renders a sprite to the screen
     *
     * @param rect   The dimensions and position of the sprite
     * @param sprite The sprite to render
     */
    public void sprite(Rect rect, Sprite sprite) {
        marble.ensurePrepared();
        GL2D.drawSprite(rect, sprite);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("Box")
     *
     * @param rect The rect to draw to
     */
    public void box(Rect rect) {
        marble.ensurePrepared();
        sprite(rect, sprBox);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("BoxSpecialLight")
     *
     * @param rect The rect to draw to
     */
    public void boxSpecialLight(Rect rect) {
        marble.ensurePrepared();
        sprite(rect, sprBoxSpecialLight);
    }

    /**
     * Renders a box using the currently selected UI spritesheet
     * spr("BoxSpecialDark")
     *
     * @param rect The rect to draw to
     */
    public void boxSpecialDark(Rect rect) {
        marble.ensurePrepared();
        sprite(rect, sprBoxSpecialDark);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("Button")
     *
     * @param rect The rect to draw to
     */
    public void button(Rect rect) {
        marble.ensurePrepared();
        sprite(rect, sprButton);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonHover")
     *
     * @param rect The rect to draw to
     */
    public void buttonHover(Rect rect) {
        marble.ensurePrepared();
        sprite(rect, sprButtonHover);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonSpecial")
     *
     * @param rect The rect to draw to
     */
    public void buttonSpecial(Rect rect) {
        marble.ensurePrepared();
        sprite(rect, sprButtonSpecial);
    }

    /**
     * Renders a button using the currently selected UI spritesheet
     * spr("ButtonSpecial")
     *
     * @param rect The rect to draw to
     */
    public void window(Rect rect) {
        marble.ensurePrepared();
        sprite(rect, sprWindow);
    }

    /**
     * Unreferences / Deletes any assets used by the render
     */
    public void destroy() {
        spriteSheet.unreference();
    }

    @Override
    public String toString() {
        return "MarbleIMRenderer{" +
                "currFont=" + font +
                ", spriteSheet=" + spriteSheet +
                '}';
    }

    /**
     * Calculates the width of the string rendered using the current font
     *
     * @param text The text to "render"
     * @return The width of text in pixels
     */
    public float getStringWidth(String text) {
        return font.getStringWidth(text);
    }
}
