package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.marble.Font;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Matrix4x4f;
import dk.sebsa.spellbook.math.Rect;
import lombok.CustomLog;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
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
    private static GLSLShaderProgram defaultShader;
    private static Mesh2D guiMesh;
    private static Matrix4x4f ortho;
    private static Color currentColor;

    /**
     * Initializes the renderer, done once pr program
     *
     * @param w The window in which it will render
     * @param s The default 2d shader
     */
    public static void init(GLFWWindow w, GLSLShaderProgram s) {
        logger.log("Initializing GL2D");

        window = w;
        defaultShader = s;

        guiMesh = Mesh2D.getQuad();
        prepareShader(defaultShader);
        logger.log("GL2D loaded");

        missingSprite = (Sprite) AssetManager.getAssetS(new Identifier("spellbook", "missing.spr"));
        ortho = Matrix4x4f.ortho(0, window.rect.width, window.rect.height, 0, -1, 1);
    }

    /**
     * Prepares a shader for 2d rendering
     *
     * @param shaderProgram The shader to prepare
     */
    public static void prepareShader(GLSLShaderProgram shaderProgram) {
        logger.log("Prepare shader for 2D rendering " + shaderProgram.getLocation().identifier());
        try {
            shaderProgram.createUniform("projection");
            shaderProgram.createUniform("offset");
            shaderProgram.createUniform("pixelScale");
            shaderProgram.createUniform("screenPos");
            shaderProgram.createUniform("color");
            shaderProgram.createUniform("useColor");
            shaderProgram.createUniform("sampler");
        } catch (Exception e) {
            logger.warn("Failed to create shader uniforms", "This might still work, if the error is from the uniforms already having been created");
            logger.stackTrace(e);
        }
        shaderProgram.initFor2D = true;
    }

    private static void changeColor(Color c) {
        if (c == currentColor) return;
        defaultShader.setUniform("color", c);
        currentColor = c;
    }

    /**
     * Prepares GL2D for rendering
     * Uses the default shader(Spellbook2d.glsl)
     */
    public static void prepare() {
        prepare(defaultShader);
    }

    /**
     * Prepares GL2D for rendering
     *
     * @param shader The shader to prepare for
     * @return The now prepared shader
     */
    public static GLSLShaderProgram prepare(GLSLShaderProgram shader) {
        if (!shader.initFor2D) prepareShader(shader);

        // Gl status
        glDisable(GL_DEPTH_TEST);

        if (window.isDirty()) ortho = Matrix4x4f.ortho(0, window.rect.width, window.rect.height, 0, -1, 1);

        // Render preparation
        defaultShader.bind();
        defaultShader.setUniform("projection", ortho);
        changeColor(Color.white);
        guiMesh.bind();
        return shader;
    }

    /**
     * Unbinds assets used for rendering with GL2D
     */
    public static void unprepare() {
        // Enable 3d
        glEnable(GL_DEPTH_TEST);

        defaultShader.unbind();
        guiMesh.unbind();
    }

    /**
     * Draws a texture with(out) texture coordinates (0,0,1,1)
     *
     * @param mat      Material to draw
     * @param drawRect Where to draw
     */
    public static void drawTextureWithTextCords(Material mat, Rect drawRect) {
        drawTextureWithTextCords(mat, drawRect, Rect.UV, guiMesh);
    }

    /**
     * Draws text
     *
     * @param text      Text to draw
     * @param drawRect Where to draw
     */
    public static void drawText(String text, Color c, Font font, Rect drawRect) {
        // Generate the rect that we can draw within
        window.rect.getIntersection(r2.set(drawRect.x, drawRect.y, drawRect.width, drawRect.height), r);

        // Set Color and Textre
        changeColor(c);
        font.getMaterial().getTexture().bind(0);
        defaultShader.setUniform("sampler", 0);
        defaultShader.setUniform("useColor", 2);

        float scale = stbtt_ScaleForPixelHeight(font.getFontType().getInfo(), font.getFontSize()); // Fontscale
        float lineHeight = (font.getAscent() - font.getDescent() + font.getLineGap()) * scale;

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1); // Pointer to the char codepoint (*int)
            FloatBuffer x = stack.floats(0.0f); // Current x pos
            FloatBuffer y = stack.floats(0.0f); // Current y pos

            STBTTAlignedQuad q = STBTTAlignedQuad.malloc(stack);
            float lineY = drawRect.y;

            for (int i = 0, to = text.length(); i < to; ) { // For all charaters
                i += Font.getCP(text, to, i, pCodePoint);
                int cp = pCodePoint.get(0); // The charater codepoint

                // newline and other weird charaters
                if (cp == '\n') {
                    y.put(0, lineY = y.get(0) + lineHeight * 0.5f);
                    x.put(0, 0.0f);
                } else if (cp < 32) continue;

                // Render the charater
                float cpX = x.get(0); // Current x pos (before the current char is rendered)
                stbtt_GetBakedQuad(font.getCdata(), Font.BITMAP_W, Font.BITMAP_H, cp - 32, x, y, q, true);

                if (font.isKerningEnabled() && i < to) {
                    Font.getCP(text, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(font.getFontType().getInfo(), cp, pCodePoint.get(0)) * scale);
                }

                float
                        x0 = q.x0(),
                        x1 = Math.min(q.x1(), r.width),
                        y0 = q.y0(),
                        y1 = Math.min(q.y1(), r.height);

                defaultShader.setUniform("offset", q.s0(), q.t0(), q.s1()-q.s0(), q.t1()-q.t0());
                defaultShader.setUniform("pixelScale", x1-x0,y1-y0);
                defaultShader.setUniform("screenPos", x0, (lineY+lineHeight)+y0);

                glDrawArrays(GL_TRIANGLES, 0, 6);
            }
        }

        // Unbind texture
        font.getMaterial().getTexture().unbind(0);
    }

    /**
     * Draws a texture with texture coordinates
     *
     * @param mat      Material to draw
     * @param drawRect Where to draw
     * @param uvRect   Texture coords
     */
    public static void drawTextureWithTextCords(Material mat, Rect drawRect, Rect uvRect) {
        drawTextureWithTextCords(mat, drawRect, uvRect, guiMesh);
    }

    /**
     * Draws a texture with texture coordinates
     *
     * @param mat      Material to draw
     * @param drawRect Where to draw
     * @param uvRect   Texture coords
     * @param mesh     Mesh to draw to
     */
    public static void drawTextureWithTextCords(Material mat, Rect drawRect, Rect uvRect, Mesh2D mesh) {
        window.rect.getIntersection(r2.set(drawRect.x, drawRect.y, drawRect.width, drawRect.height), r);

        // uvreact
        float x = uvRect.x + (((r.x - drawRect.x) / drawRect.width) * uvRect.width);
        float y = uvRect.y + (((r.y - drawRect.y) / drawRect.height) * uvRect.height);
        u.set(x, y, (r.width / drawRect.width) * uvRect.width, (r.height / drawRect.height) * uvRect.height);

        // Draw
        changeColor(mat.getColor());
        if (mat.getTexture() != null) mat.getTexture().bind(0);
        else missingSprite.getMaterial().getTexture().bind(0);

        defaultShader.setUniform("sampler", 0);
        defaultShader.setUniform("useColor", mat.isTextured() ? 0 : 1);
        defaultShader.setUniform("offset", u.x, u.y, u.width, u.height);
        defaultShader.setUniform("pixelScale", r.width, r.height);
        defaultShader.setUniform("screenPos", r.x, r.y);

        glDrawArrays(GL_TRIANGLES, 0, 6);
        if (mat.getTexture() != null) mat.getTexture().unbind(0);
        else missingSprite.getMaterial().getTexture().unbind(0);
    }

    /**
     * Renders a sprite to the screen
     *
     * @param r Dimension and position of the sprite
     * @param e The sprite (if null a missing texutre sprite will be renderd)
     */
    public static void drawSprite(Rect r, Sprite e) {
        if (e == null) e = missingSprite;
        //Cache a short variable for the texture, just so we only have to type a character anytime we use it
        Rect uv = e.getUV();

        //Get the top left corner of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(r.x, r.y, e.getPadding().x, e.getPadding().y);   // TL
        rect2.set(uv.x, uv.y, e.getPaddingUV().x, e.getPaddingUV().y); // TLU
        drawTextureWithTextCords(e.getMaterial(), rect1, rect2);

        //Get the top right corner of the box using corresponding padding values and draw it using a texture drawing method
        tr.set((r.x + r.width) - e.getPadding().width, r.y, e.getPadding().width, e.getPadding().y);    // TR
        tru.set((uv.x + uv.width) - e.getPaddingUV().width, uv.y, e.getPaddingUV().width, e.getPaddingUV().y); // TRU
        drawTextureWithTextCords(e.getMaterial(), tr, tru);

        //Get the bottom left corner of the box using corresponding padding values and draw it using a texture drawing method
        bl.set(r.x, (r.y + r.height) - e.getPadding().height, e.getPadding().x, e.getPadding().height); // BL
        blu.set(uv.x, (uv.y + uv.height) - e.getPaddingUV().height, e.getPaddingUV().x, e.getPaddingUV().height); //BLU
        drawTextureWithTextCords(e.getMaterial(), bl, blu);

        //Get the bottom right corner of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(tr.x, bl.y, e.getPadding().width, e.getPadding().height); // BR
        rect2.set(tru.x, blu.y, e.getPaddingUV().width, e.getPaddingUV().height); // BRU
        drawTextureWithTextCords(e.getMaterial(), rect1, rect2);

        //Get the left side of the box using corresponding padding values and draw it using a texture drawing method
        l.set(r.x, r.y + e.getPadding().y, e.getPadding().x, r.height - (e.getPadding().y + e.getPadding().height)); // L
        lu.set(uv.x, uv.y + e.getPaddingUV().y, e.getPaddingUV().x, uv.height - (e.getPaddingUV().y + e.getPaddingUV().height)); //LU
        drawTextureWithTextCords(e.getMaterial(), l, lu);

        //Get the right side of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(tr.x, r.y + e.getPadding().y, e.getPadding().width, l.height); // RI
        rect2.set(tru.x, lu.y, e.getPaddingUV().width, lu.height); // RU
        drawTextureWithTextCords(e.getMaterial(), rect1, rect2);

        //Get the top of the box using corresponding padding values and draw it using a texture drawing method
        ti.set(r.x + e.getPadding().x, r.y, r.width - (e.getPadding().x + e.getPadding().width), e.getPadding().y); // TI
        tu.set(uv.x + e.getPaddingUV().x, uv.y, uv.width - (e.getPaddingUV().x + e.getPaddingUV().width), e.getPaddingUV().y); // TU
        drawTextureWithTextCords(e.getMaterial(), ti, tu);

        //Get the bottom of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(ti.x, bl.y, ti.width, e.getPadding().height); // B
        rect2.set(tu.x, blu.y, tu.width, e.getPaddingUV().height); // BU
        drawTextureWithTextCords(e.getMaterial(), rect1, rect2);

        //Get the center of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(ti.x, l.y, ti.width, l.height); // C
        rect2.set(tu.x, lu.y, tu.width, lu.height); // CU
        drawTextureWithTextCords(e.getMaterial(), rect1, rect2);
    }

    /**
     * Unreferences assets uses by GL2d
     */
    public static void cleanup() {
        guiMesh.destroy();
        defaultShader.unreference();
        missingSprite.unreference();
    }
}
