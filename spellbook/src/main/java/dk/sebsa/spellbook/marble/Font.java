package dk.sebsa.spellbook.marble;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.graphics.opengl.Material;
import dk.sebsa.spellbook.graphics.opengl.Texture;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.stb.STBTruetype.stbtt_ScaleForPixelHeight;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * An instance of a font that can be used to render text
 *
 * @author sebs
 * @since 1.0.0
 */
@Getter
public class Font {
    private final HashMap<Byte, Glyph> charTable = new HashMap<>();
    private Texture texture;
    private Material material;
    private FontType fontType;
    private int fontSize;
    private STBTTBakedChar.Buffer cdata;
    private int ascent;
    private int descent;
    private int lineGap;


    public static int BITMAP_W = 1024;
    public static int BITMAP_H = 1024;

    public Font(Identifier fontType, int fontSize) {
        this.fontType = (FontType) AssetManager.getAssetS(fontType);
        this.fontSize = fontSize;
    }

    public ByteBuffer genBitMap(int BITMAP_W, int BITMAP_H) {
        STBTTBakedChar.Buffer cd = STBTTBakedChar.malloc(1024);
        ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
        stbtt_BakeFontBitmap(fontType.ttf, fontSize, bitmap, BITMAP_W, BITMAP_H, 32, cd);
        cdata = cd;
        return bitmap;
    }

    /**
     * Generates the Font
     */
    public void generateFont(Texture.TextureInfo textureInfo) {
        texture = new Texture().set(textureInfo);
        material = new Material(Color.white, texture);
        ascent = fontType.getAscent();
        descent = fontType.getDescent();
        lineGap = fontType.getLineGap();
    }

    @Setter
    private boolean kerningEnabled = true;

    public float getStringWidth(String text) {
        int width = 0;
        int to = text.length();

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint       = stack.mallocInt(1);
            IntBuffer pAdvancedWidth   = stack.mallocInt(1);
            IntBuffer pLeftSideBearing = stack.mallocInt(1);

            int i = 0;
            while (i < to) {
                i += getCP(text, to, i, pCodePoint);
                int cp = pCodePoint.get(0);

                stbtt_GetCodepointHMetrics(getFontType().getInfo(), cp, pAdvancedWidth, pLeftSideBearing);
                width += pAdvancedWidth.get(0);

                if (kerningEnabled && i < to) {
                    getCP(text, to, i, pCodePoint);
                    width += stbtt_GetCodepointKernAdvance(getFontType().getInfo(), cp, pCodePoint.get(0));
                }
            }
        }

        return width * stbtt_ScaleForPixelHeight(getFontType().getInfo(), fontSize);
    }

    /**
     * Gets the codepoint for a charater in a string
     *
     * @param text The text to get chars form
     * @param to   The length of the text
     * @param i    The index of the char
     * @param cpOut The codepoint of the char at i or the codepoiont(i,i+1)
     * @return How much to advance i (mostly 1, but might be 2 if combined charater are used)
     */
    public static int getCP(String text, int to, int i, IntBuffer cpOut) {
        char c1 = text.charAt(i);
        if (Character.isHighSurrogate(c1) && i + 1 < to) {
            char c2 = text.charAt(i + 1);
            if (Character.isLowSurrogate(c2)) {
                cpOut.put(0, Character.toCodePoint(c1, c2));
                return 2;
            }
        }
        cpOut.put(0, c1);
        return 1;
    }

    public void destroy() {
        texture.destroy();
        cdata.free();
        fontType.unreference();
    }

    /**
     * Represents a single glyph that can be rendered
     *
     * @param pos   The position of the glyph in texture coordinates
     * @param size  The proportional size of the glyph
     * @param scale The size of the glyph in pixels
     * @author sebs
     * @since 1.0.0
     */
    public record Glyph(Vector2f pos, Vector2f size, Vector2f scale) {
    }
}
