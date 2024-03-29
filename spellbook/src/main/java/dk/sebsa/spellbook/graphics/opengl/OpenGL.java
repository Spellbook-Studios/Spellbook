package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.loading.AssetLocation;
import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.core.events.EngineLoadEvent;
import dk.sebsa.spellbook.core.events.EngineRenderEvent;
import dk.sebsa.spellbook.graphics.RenderAPI;
import dk.sebsa.spellbook.marble.Font;
import dk.sebsa.spellbook.marble.FontType;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.CustomLog;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

/**
 * Renders with OpenGL
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class OpenGL extends RenderAPI {
    private Color clearColor;
    private RenderPipeline pipeline;
    private boolean capRender2D;

    @Override
    public void loadTexture(Texture t, AssetLocation l) {
        try {
            t.set(loadTexture(l));
        } catch (IOException e) {
            Spellbook.instance.error("Failed to load texture, IOException: " + l, false);
        }
    }

    @Override
    public void bindTexture(Texture t, int textureUnit) {
        glActiveTexture(GL_TEXTURE0 + textureUnit);
        glBindTexture(GL_TEXTURE_2D, t.getId());
    }

    @Override
    public void unbindTexture(int textureUnit) {
        glActiveTexture(GL_TEXTURE0 + textureUnit);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void generateFontType(FontType fontType) {
        try {
            fontType.loadSTBTTFontInfo();
        } catch (IOException e) {
            Spellbook.instance.error("IOException when generating fonttype " + fontType.getLocation(), false);
        }
    }

    @Override
    public void generateFont(Font f) {
        ByteBuffer bitmap = f.genBitMap();

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, Font.BITMAP_W, Font.BITMAP_H, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        f.generateFont(new Texture.TextureInfo(Font.BITMAP_W, Font.BITMAP_H, texID));
    }

    @Override
    public void destroy(Texture texture) {
        glDeleteTextures(texture.getId());
    }

    /**
     * Loads a texture from a file
     *
     * @param location the location of the texture file
     * @return The information about this OpenGL texture
     */
    private Texture.TextureInfo loadTexture(AssetLocation location) throws IOException {
        ByteBuffer data;

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        InputStream is = FileUtils.loadFile(location);

        // Load texture from stream
        byte[] bytes = new byte[8000];
        int curByte;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while ((curByte = is.read(bytes)) != -1) {
            bos.write(bytes, 0, curByte);
        }
        is.close();

        bytes = bos.toByteArray();
        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
        buffer.put(bytes).flip();
        data = stbi_load_from_memory(buffer, widthBuffer, heightBuffer, channelsBuffer, 4);


        // Create the OpenGL texture
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId); // Lets us work with the texture

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Upload the texture data
        int width = widthBuffer.get();
        int height = heightBuffer.get();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, data);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

        assert data != null;
        stbi_image_free(data);

        return new Texture.TextureInfo(width, height, textureId);
    }

    @Override
    public void setup(EngineLoadEvent e) {
        logger.log("Init OpenGL support");
        // Init OpenGL
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        glfwMakeContextCurrent(e.moduleCore.getWindow().getId());
        GL.createCapabilities();
        glEnable(GL_DEBUG_OUTPUT);
        logger.log("Setup GL functionality");
        // Enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Get graphics card
        Spellbook.graphicsCard = glGetString(GL_RENDER) + " " + glGetString(GL_VENDOR);
        logger.log("Graphics Card: " + Spellbook.graphicsCard);

        clearColor = e.capabilities.clearColor;

        // BuildRenderPipeline
        var event = new EngineBuildRenderPipelineEvent(e.moduleCore, e.capabilities);
        var builder = ((EngineBuildRenderPipelineEvent) Spellbook.instance.getEventBus().engine(event)).builder;

        pipeline = builder.build();

        // Init GL2D and Sprite2D
        GL2D.init(e.moduleCore.getWindow());

        capRender2D = e.capabilities.render2D;
        if (capRender2D) {
            Sprite2D.init(e);
        }

        // Debug info
        logger.log("OpenGL version: " + glGetString(GL_VERSION));
        logger.trace("OpenGL renderer: " + glGetString(GL_RENDERER));
        logger.trace("OpenGL shading language version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
        int[] context_profile = new int[1];
        glGetIntegerv(GL_CONTEXT_PROFILE_MASK, context_profile);
        if (context_profile[0] == GL_CONTEXT_CORE_PROFILE_BIT) {
            logger.trace("GL_CONTEXT_PROFILE_MASK: GL_CONTEXT_CORE_PROFILE_BIT ");
        }
        if (context_profile[0] == GL_CONTEXT_COMPATIBILITY_PROFILE_BIT) {
            logger.trace("GL_CONTEXT_PROFILE_MASK: GL_CONTEXT_COMPATIBILITY_PROFILE_BIT");
        }
        logger.trace("GL_CONTEXT_PROFILE_MASK: " + context_profile[0]);

        int window_major_version = glfwGetWindowAttrib(e.moduleCore.getWindow().getId(), GLFW_CONTEXT_VERSION_MAJOR);
        int window_minor_version = glfwGetWindowAttrib(e.moduleCore.getWindow().getId(), GLFW_CONTEXT_VERSION_MINOR);

        logger.trace("Window context version: " + window_major_version + "." + window_minor_version);
    }

    @Override
    public void destroy() {
        GL2D.cleanup();
        pipeline.destroy();

        if (capRender2D) {
            Sprite2D.destroy();
        }
    }

    @Override
    public void renderFrame(EngineRenderEvent e) {
        glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
        glViewport(0, 0, (int) e.window.rect.width, (int) e.window.rect.height);

        pipeline.render(e);

        glfwSwapBuffers(e.window.getId());
        e.window.endFrame(); // TODO: PLACE THIS BETTER
    }
}
