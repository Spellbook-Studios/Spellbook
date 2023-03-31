package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Matrix4x4f;
import dk.sebsa.spellbook.math.Rect;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;

/**
 * A renderer for rendering 2D sprites
 * @author sebs
 * @since 0.0.1
 */
public class GL2D {
    private static ClassLogger logger;
    private static GLFWWindow window;
    private static AssetReference shaderR;
    private static GLSLShaderProgram defaultShader;
    private static Mesh2D guiMesh;
    private static Matrix4x4f ortho;
    private static Color currentColor;

    /**
     * Initializes the renderer, done once pr program
     * @param w The window in which it will render
     * @param s The default 2d shader
     */
    public static void init(GLFWWindow w, AssetReference s) {
        logger = new ClassLogger(new GL2D(), Spellbook.getLogger());
        logger.log("Initializing GL2D");

        window = w;
        shaderR = s;
        defaultShader = s.get();

        guiMesh = Mesh2D.getQuad();
        prepareShader(defaultShader);
        logger.log("GL2D loaded");
    }

    /**
     * Prepares a shader for 2d rendering
     * @param shaderProgram The shader to prepare
     */
    public static void prepareShader(GLSLShaderProgram shaderProgram) {
        logger.log("Prepare shader for 2D rendering " + shaderR.name);
        try {
            shaderProgram.createUniform("projection", logger);
            shaderProgram.createUniform("offset", logger);
            shaderProgram.createUniform("pixelScale", logger);
            shaderProgram.createUniform("screenPos", logger);
            shaderProgram.createUniform("color", logger);
            shaderProgram.createUniform("useColor", logger);
        } catch (Exception e) { e.printStackTrace(); }
        shaderProgram.initFor2D = true;
    }

    private static void changeColor(Color c) {
        if(c==currentColor) return;
        defaultShader.setUniform("color", c);
        currentColor = c;
    }

    /**
     * Prepares GL2D for rendering
     * Uses the default shader(Spellbook2d.glsl)
     */
    public static void prepare() { prepare(defaultShader); }

    /**
     * Prepares GL2D for rendering
     * @param shader The shader to prepare for
     */
    public static void prepare(GLSLShaderProgram shader) {
        if(!shader.initFor2D) prepareShader(shader);

        // Disable 3d
        glDisable(GL_DEPTH_TEST);

        if(window.isDirty()) ortho = Matrix4x4f.ortho(0, window.getWidth(), window.getHeight(), 0, -1, 1);

        // Render preparation
        defaultShader.bind();
        defaultShader.setUniform("projection", ortho);
        changeColor(Color.white);
        guiMesh.bind();
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

    private static final Rect fullUV = new Rect(0,0,1,1);

    /**
     * Draws a texture with(out) texture coordinates (0,0,1,1)
     * @param mat Material to draw
     * @param drawRect Where to draw
     */
    public static void drawTextureWithTextCords(Material mat, Rect drawRect) { drawTextureWithTextCords(mat, drawRect, fullUV, guiMesh); }

    /**
     * Draws a texture with texture coordinates
     * @param mat Material to draw
     * @param drawRect Where to draw
     * @param uvRect Texture coords
     */
    public static void drawTextureWithTextCords(Material mat, Rect drawRect, Rect uvRect) { drawTextureWithTextCords(mat, drawRect, uvRect, guiMesh); }

    private static final Rect u = new Rect(0,0,0,0);
    private static final Rect r = new Rect(0,0,0,0);
    private static final Rect r2 = new Rect(0,0,0,0);

    /**
     * Draws a texture with texture coordinates
     * @param mat Material to draw
     * @param drawRect Where to draw
     * @param uvRect Texture coords
     * @param mesh Mesh to draw to
     */
    public static void drawTextureWithTextCords(Material mat, Rect drawRect, Rect uvRect, Mesh2D mesh) {
        window.rect.getIntersection(r2.set(drawRect.x, drawRect.y, drawRect.width, drawRect.height), r);

        // uvreact
        float x = uvRect.x + (((r.x - drawRect.x) / drawRect.width) * uvRect.width);
        float y = uvRect.y + (((r.y - drawRect.y) / drawRect.height) * uvRect.height);
        u.set(x, y, (r.width / drawRect.width) * uvRect.width, (r.height / drawRect.height) * uvRect.height);

        // Draw
        changeColor(mat.getColor());
        if(mat.getTexture() != null) mat.getTexture().bind();
        // TODO: else { noTexture.bind(); u.set(0,0,1,1); }
        defaultShader.setUniform("useColor", mat.isTextured() ? 0 : 1);
        defaultShader.setUniform("offset", u.x, u.y, u.width, u.height);
        defaultShader.setUniform("pixelScale", r.width, r.height);
        defaultShader.setUniform("screenPos", r.x, r.y);

        GL20.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
        if(mat.getTexture() != null) mat.getTexture().unbind();
    }

    /**
     * Unreferences assets uses by GL2d
     */
    public static void cleanup() {
        guiMesh.destroy();
        defaultShader = null;
        shaderR.unRefrence();
    }
}
