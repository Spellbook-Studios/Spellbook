package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;

import dk.sebsa.spellbook.io.GLFWWindow;
import dk.sebsa.spellbook.math.Rect;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Frame Buffer Object
 * @since 1.0.0
 * @author sebs
 */
public class FBO {
    private final int frameBufferID;
    private final int depthBufferID;
    private final Texture texture;
    public final Material material;

    /**
     * Width of framebuffer
     */
    public final int width;

    /**
     * Height of frameBuffer
     */
    public final int height;

    private final GLFWWindow window;

    private void trace(Object o) { Spellbook.getLogger().trace(o); }

    /**
     * @param w Width of buffer
     * @param h Height of buffer
     * @param window Window of program
     */
    public FBO(int w, int h, GLFWWindow window) {
        this.width = w;
        this.height = h;
        this.window = window;

        frameBufferID = createFrameBuffer();
        depthBufferID = createDepthBufferAttachment();

        int textureID = createTextureAttachment();
        texture = new Texture().set(new Texture.TextureInfo(width, height, textureID));
        material = new Material(texture);
        unBind();
    }

    /**
     * Binds the GL_RENDERBUFFER and GL_GRAMEBUFFER
     * Resets glViewPort
     */
    public void bindFrameBuffer() {
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferID);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBufferID);
        glViewport(0,0,width,height);
    }

    /**
     * Unbinds the GL_RENDERBUFFER and GL_GRAMEBUFFER
     * Resets glViewPort
     */
    public void unBind() {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        glViewport(0,0,window.getWidth(),window.getHeight());
    }

    private int createTextureAttachment() {
        trace("Gen Texture Attachment");
        int texture = GL11.glGenTextures();

        GL11.glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);

        return texture;
    }

    private int createDepthBufferAttachment() {
        trace("Gen DepthBuffer Attachment");
        int buffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, buffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,buffer);
        return buffer;
    }

    private int createFrameBuffer() {
        trace("Creating FrameBuffer");
        int buffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        return buffer;
    }

    /**
     * Destroys the FBO
     */
    public void destroy() {
        GL30.glDeleteFramebuffers(frameBufferID);
        texture.destroy();
    }

    /**
     * Render an FBO
     * @param fbo The fbo to render
     * @param r Where to render it
     * @param t Texture coords (most often (0,0,1,1);
     */
    public static void renderFBO(FBO fbo, Rect r, Rect t) {
        if(fbo == null) return;
        GL2D.prepare();
        GL2D.drawTextureWithTextCords(fbo.material, r, t);
        GL2D.unprepare();
    }

    /**
     * Render a list of FBO, with element 0 being first
     * @param fbos The fbo to render
     * @param r Where to render it
     * @param t Texture coords (most often (0,0,1,1);
     */
    public static void renderFBOS(List<FBO> fbos, Rect r, Rect t) {
        if(fbos == null || fbos.isEmpty()) return;
        GL2D.prepare();
        for(FBO fbo : fbos) GL2D.drawTextureWithTextCords(fbo.material, r, t);
        GL2D.unprepare();
    }
}
