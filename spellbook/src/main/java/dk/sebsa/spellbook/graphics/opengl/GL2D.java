package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.graphics.opengl.renderer.GLSpriteRenderer;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.marble.Font;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import lombok.CustomLog;
import lombok.Getter;
import org.joml.Matrix4f;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * A renderer for rendering 2D sprites
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class GL2D {
    private static final Rect u = new Rect(0, 0, 0, 0);
    private static final Rect r = new Rect(0, 0, 0, 0);
    private static final Rect r2 = new Rect(0, 0, 0, 0);
    private static final Rect rect1 = new Rect();   // This one is used for multiple things
    private static final Rect rect2 = new Rect();   // This one is used for multiple things
    private static final Rect tr = new Rect();
    private static final Rect tru = new Rect();
    private static final Rect ti = new Rect();
    private static final Rect tu = new Rect();
    private static final Rect bl = new Rect();
    private static final Rect blu = new Rect();
    private static final Rect l = new Rect();
    private static final Rect lu = new Rect();
    /**
     * The missingSprite sprite from Spellbook internal assets
     * In rendering, it can be used when there is an absence of other textures
     */
    public static Sprite missingSprite;
    private static GLFWWindow window;
    private static Matrix4f ortho;
    private static GLSpriteRenderer textRenderer;
    private static GLSpriteRenderer spriteRenderer;
    @Getter
    private static boolean isPrepared;

    /**
     * Initializes the renderer, done once pr program
     *
     * @param w The window in which it will render
     */
    public static void init(GLFWWindow w) {
        logger.log("Initializing GL2D");
        window = w;

        missingSprite = (Sprite) AssetManager.getAssetS(new Identifier("spellbook", "missing.spr"));
        ortho = new Matrix4f().ortho(0, window.rect.width, window.rect.height, 0,-1, 1);

        textRenderer = new GLSpriteRenderer(new Identifier("spellbook", "shaders/SpellbookText.glsl"));
        spriteRenderer = new GLSpriteRenderer(new Identifier("spellbook", "shaders/Spellbook2d.glsl"));
    }

    /**
     * Prepares GL2D for rendering
     * @return Returns the prepared renderer
     */
    public static GLSpriteRenderer prepare() {
        return prepare(window.rect);
    }


    /**
     * Prepares GL2D for rendering
     *
     * @param resolution THe resolution to render for
     * @return Returns the prepared renderer
     */
    public static GLSpriteRenderer prepare(Rect resolution) {
        // Gl status
        glDisable(GL_DEPTH_TEST);

        if (window.isDirty()) ortho = new Matrix4f().ortho(0, resolution.width, resolution.height, 0,-1, 1);
        spriteRenderer.begin(ortho);

        isPrepared = true;
        return spriteRenderer;
    }

    /**
     * Unbinds assets used for rendering with GL2D
     */
    public static void unprepare() {
        spriteRenderer.end();
        isPrepared = false;
    }

    /**
     * Draws a texture with(out) texture coordinates (0,0,1,1)
     *
     * @param mat      Material to draw
     * @param drawRect Where to draw
     */
    public static void drawTextureWithTextCords(Material mat, Rect drawRect) {
        drawTextureWithTextCords(mat, drawRect, Rect.UV);
    }

    /**
     * Draws a texture with texture coordinates
     *
     * @param mat      Material to draw
     * @param drawRect Where to draw
     * @param uvRect   Texture coords
     */
    public static void drawTextureWithTextCords(Material mat, Rect drawRect, Rect uvRect) {
        Material material;
        if (mat.getTexture() != null) material = mat;
        else material = missingSprite.getMaterial();

        spriteRenderer.setMaterial(material);
        drawTextureWithTextCords(drawRect, uvRect);
    }

    private static void drawTextureWithTextCords(Rect drawRect, Rect uvRect) {
        window.rect.getIntersection(r2.set(drawRect.x, drawRect.y, drawRect.width, drawRect.height), r);
        // uvreact
        float x = uvRect.x + (((r.x - drawRect.x) / drawRect.width) * uvRect.width);
        float y = uvRect.y + (((r.y - drawRect.y) / drawRect.height) * uvRect.height);
        u.set(x, y, (r.width / drawRect.width) * uvRect.width, (r.height / drawRect.height) * uvRect.height);
        spriteRenderer.drawQuad(r,u);
    }

    /**
     * Renders a sprite to the screen
     *
     * @param r Dimension and position of the sprite
     * @param e The sprite (if null a missing texutre sprite will be renderd)
     */
    public static void drawSprite(Rect r, Sprite e) {
        if (e == null) e = missingSprite;
        spriteRenderer.setMaterial(e.getMaterial());
        //Cache a short variable for the texture, just so we only have to type a character anytime we use it
        Rect uv = e.getUV();

        //Get the top left corner of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(r.x, r.y, e.getPadding().x, e.getPadding().y);   // TL
        rect2.set(uv.x, uv.y, e.getPaddingUV().x, e.getPaddingUV().y); // TLU
        drawTextureWithTextCords(rect1, rect2);

        //Get the top right corner of the box using corresponding padding values and draw it using a texture drawing method
        tr.set((r.x + r.width) - e.getPadding().width, r.y, e.getPadding().width, e.getPadding().y);    // TR
        tru.set((uv.x + uv.width) - e.getPaddingUV().width, uv.y, e.getPaddingUV().width, e.getPaddingUV().y); // TRU
        drawTextureWithTextCords(tr, tru);

        //Get the bottom left corner of the box using corresponding padding values and draw it using a texture drawing method
        bl.set(r.x, (r.y + r.height) - e.getPadding().height, e.getPadding().x, e.getPadding().height); // BL
        blu.set(uv.x, (uv.y + uv.height) - e.getPaddingUV().height, e.getPaddingUV().x, e.getPaddingUV().height); //BLU
        drawTextureWithTextCords(bl, blu);

        //Get the bottom right corner of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(tr.x, bl.y, e.getPadding().width, e.getPadding().height); // BR
        rect2.set(tru.x, blu.y, e.getPaddingUV().width, e.getPaddingUV().height); // BRU
        drawTextureWithTextCords(rect1, rect2);

        //Get the left side of the box using corresponding padding values and draw it using a texture drawing method
        l.set(r.x, r.y + e.getPadding().y, e.getPadding().x, r.height - (e.getPadding().y + e.getPadding().height)); // L
        lu.set(uv.x, uv.y + e.getPaddingUV().y, e.getPaddingUV().x, uv.height - (e.getPaddingUV().y + e.getPaddingUV().height)); //LU
        drawTextureWithTextCords(l, lu);

        //Get the right side of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(tr.x, r.y + e.getPadding().y, e.getPadding().width, l.height); // RI
        rect2.set(tru.x, lu.y, e.getPaddingUV().width, lu.height); // RU
        drawTextureWithTextCords(rect1, rect2);

        //Get the top of the box using corresponding padding values and draw it using a texture drawing method
        ti.set(r.x + e.getPadding().x, r.y, r.width - (e.getPadding().x + e.getPadding().width), e.getPadding().y); // TI
        tu.set(uv.x + e.getPaddingUV().x, uv.y, uv.width - (e.getPaddingUV().x + e.getPaddingUV().width), e.getPaddingUV().y); // TU
        drawTextureWithTextCords(ti, tu);

        //Get the bottom of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(ti.x, bl.y, ti.width, e.getPadding().height); // B
        rect2.set(tu.x, blu.y, tu.width, e.getPaddingUV().height); // BU
        drawTextureWithTextCords(rect1, rect2);

        //Get the center of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(ti.x, l.y, ti.width, l.height); // C
        rect2.set(tu.x, lu.y, tu.width, lu.height); // CU
        drawTextureWithTextCords(rect1, rect2);
    }

    /**
     * Draws text
     *
     * @param text      Text to draw
     * @param c         The desired color of the text
     * @param font      Font to render with
     * @param drawRect  Where to draw
     */
    public static void drawText(String text, Color c, Font font, Rect drawRect) {
        if(isPrepared) spriteRenderer.end();
        window.rect.getIntersection(r2.set(drawRect.x, drawRect.y, drawRect.width, drawRect.height), r);
        textRenderer.begin(ortho);
        textRenderer.setMaterial(font.getMaterial().setColor(c));

        float scale = stbtt_ScaleForPixelHeight(font.getFontType().getInfo(), font.getFontSize()); // Fontscale
        float lineHeight = (font.getAscent() - font.getDescent() + font.getLineGap()) * scale;

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1); // Pointer to the char codepoint (*int)
            FloatBuffer x = stack.floats(r.x); // Current x pos
            //noinspection,SuspiciousNameCombination
            FloatBuffer y = stack.floats(r.y); // Current y pos

            STBTTAlignedQuad q = STBTTAlignedQuad.malloc(stack);

            for (int i = 0, to = text.length(); i < to; ) { // For all charaters
                i += Font.getCP(text, to, i, pCodePoint);
                int cp = pCodePoint.get(0); // The charater codepoint

                // newline and other weird charaters
                if (cp == '\n') {
                    y.put(0, y.get(0) + lineHeight);
                    x.put(0, 0.0f);
                } else if (cp < 32) continue;

                // Render the charater
                stbtt_GetBakedQuad(font.getCdata(), Font.BITMAP_W, Font.BITMAP_H, cp - 32, x, y, q, true);

                if (font.isKerningEnabled() && i < to) {
                    Font.getCP(text, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(font.getFontType().getInfo(), cp, pCodePoint.get(0)) * scale);
                }

                float
                        x0 = q.x0(),
                        x1 = q.x1(),
                        y0 = q.y0(),
                        y1 = q.y1();

                // Fix warping maybe check pre commit
                r.getIntersection(r2.set(x0,(lineHeight)+y0, x1-x0, y1-y0), rect1);
                textRenderer.drawQuad(rect1, new Rect(q.s0(), q.t0(), q.s1()-q.s0(), q.t1()-q.t0()));
            }
        }

        textRenderer.end();
        if(isPrepared) spriteRenderer.begin(ortho);
    }

    /**
     * Unreferences assets uses by GL2D
     */
    public static void cleanup() {
        missingSprite.unreference();
        textRenderer.destroy();
        spriteRenderer.destroy();
    }
}
