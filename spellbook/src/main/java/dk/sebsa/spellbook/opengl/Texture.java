package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.loading.AssetLocation;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;
import org.lwjgl.BufferUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

/**
 * Representation of a texutere, this includes as assets but also ones created dynamically (e.g. fbos)
 * @author sebs
 * @since 1.0.0
 */
public class Texture extends Asset {
    private int id;
    @Getter private float width, height;


    public void load() {
        try {
            set(loadTexture(location));
        } catch (IOException e) {
            Spellbook.instance.error("Failed to load texutre, IOException: " + location, false);
        }
    }

    /**
     * Sets the values of this texture the one of the texture info
     * @param ti The texture info to load from
     * @return This texture
     */
    public Texture set(TextureInfo ti) {
        this.width = ti.width;
        this.height = ti.height;
        this.id = ti.textureId;
        return this;
    }

    /**
     * Loads a texture from a file
     * @param location the location of the texture file
     * @return The information about this OpenGL texture
     */
    private TextureInfo loadTexture(AssetLocation location) throws IOException {
        ByteBuffer data;

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        InputStream is = FileUtils.loadFile(location.location());

        // Load texture from stream
        byte[] bytes = new byte[8000];
        int curByte;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while((curByte = is.read(bytes)) != -1) { bos.write(bytes, 0, curByte);}
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

        return new TextureInfo(width, height, textureId);
    }

    @Override
    public void destroy() {
        glDeleteTextures(id);
    }

    /**
     * Bind the texture to GL_TEXTURE_2D
     */
    public void bind() { glBindTexture(GL_TEXTURE_2D, id); }

    /**
     * Unbinds the GL_TEXTURE_2D
     */
    public void unbind() { glBindTexture(GL_TEXTURE_2D, 0); }

    /**
     * Used to load in TextureData
     * @param width The width of the texture
     * @param height The height of the texture
     * @param textureId The OpenGL texture id
     */
    public record TextureInfo(int width, int height, int textureId) {}
}
