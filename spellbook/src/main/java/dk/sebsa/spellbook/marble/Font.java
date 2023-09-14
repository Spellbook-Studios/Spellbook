package dk.sebsa.spellbook.marble;

import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Vector2f;
import dk.sebsa.spellbook.opengl.Material;
import dk.sebsa.spellbook.opengl.Texture;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * A font capable of rendering text
 * @author sebsn
 * @since 0.0.1
 */
public class Font {
    private BufferedImage bufferedImage;
    private final java.awt.Font baseFont;
    private Vector2f imageSize;
    private FontMetrics fontMetrics;

    @Getter private final HashMap<Byte, Glyph> charTable = new HashMap<>();
    @Getter private Texture texture;
    @Getter private Material material;
    @Getter private float fontMaxHeight;

    /**
     * Creates a font using AWT to generate the font textures
     * @param baseFont The AWT font to use
     */
    public Font(java.awt.Font baseFont) {
        this.baseFont = baseFont;
        generateFont();
    }

    private void generateFont() {
        GraphicsConfiguration graphCon = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Graphics2D graphics = graphCon.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
        graphics.setFont(baseFont);

        fontMetrics = graphics.getFontMetrics();
        fontMaxHeight = (float) (fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent());
        imageSize = new Vector2f(2048, 2048);
        bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage((int) imageSize.x, (int) imageSize.y, Transparency.TRANSLUCENT);

        int textureId = glGenTextures();
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (int)imageSize.x, (int)imageSize.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, generateImage());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        texture = new Texture().set(new Texture.TextureInfo((int)imageSize.x, (int)imageSize.y, textureId));
        material = new Material(Color.white, texture);
    }

    private ByteBuffer generateImage() {
        Graphics2D graphics2d = (Graphics2D) bufferedImage.getGraphics();
        graphics2d.setFont(baseFont);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawCharacters(graphics2d);
        return createBuffer();
    }

    private static final String ISO_8559_1 = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®¯°±³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïððòóôõö÷øùúûüýþÿ";
    private void drawCharacters(Graphics2D graphics2d) {
        float tempX = 0;
        int tempY = 0;
        Charset charset = Charset.forName("ISO_8859_1");
        byte[] chars = charset.encode(ISO_8559_1).array();

        for(int i = 0; i < chars.length; i++) {
            char c = (char) chars[i];
            float charWidth = fontMetrics.charWidth(c);

            float advance = charWidth + 8;

            if(tempX + advance > imageSize.x) {
                tempX = 0;
                tempY += 1;
            }

            charTable.put(chars[i], new Glyph(new Vector2f(tempX / imageSize.x, (tempY * fontMaxHeight) / imageSize.y), new Vector2f(charWidth / imageSize.x, fontMaxHeight/imageSize.y), new Vector2f(charWidth, fontMaxHeight)));
            graphics2d.drawString(String.valueOf(ISO_8559_1.charAt(i)), tempX, fontMetrics.getMaxAscent() + (fontMaxHeight* tempY));
            tempX += advance;
        }
    }

    private ByteBuffer createBuffer() {
        int w = (int)imageSize.x;
        int h = (int)imageSize.y;
        int[] pixels = new int[w*h];

        bufferedImage.getRGB(0, 0, w, h, pixels, 0, w);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(w * h * 4);

        for (int pixel : pixels) {
            byteBuffer.put((byte) ((pixel >> 16) & 0xFF));    // Red
            byteBuffer.put((byte) ((pixel >> 8) & 0xFF));    // Green
            byteBuffer.put((byte) (pixel >> 31));        // Blue
            byteBuffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    /**
     * Represents a single glyph that can be rendered
     * @author sebs
     * @since 0.0.1
     * @param pos The position of the glyph in texture coordinates
     * @param size The proportional size of the glyph
     * @param scale The size of the glyph in pixels
     */
    public record Glyph(Vector2f pos, Vector2f size, Vector2f scale) { }
}
