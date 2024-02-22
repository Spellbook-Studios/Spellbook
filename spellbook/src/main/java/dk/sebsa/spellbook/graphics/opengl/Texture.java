package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import lombok.Getter;

/**
 * Representation of a texutere, this includes as assets but also ones created dynamically (e.g. fbos)
 *
 * @author sebs
 * @since 1.0.0
 */
public class Texture extends Asset {
    @Getter
    private int id;
    @Getter
    private float width, height;

    @Override
    public void load() {
        Spellbook.instance.getRenderer().queue(() -> Spellbook.instance.getRenderer().loadTexture(this, location));
    }

    /**
     * Sets the values of this texture the one of the texture info
     *
     * @param ti The texture info to load from
     * @return This texture
     */
    public Texture set(TextureInfo ti) {
        this.width = ti.width;
        this.height = ti.height;
        this.id = ti.textureId;
        return this;
    }

    @Override
    public void destroy() {
        Spellbook.instance.getRenderer().queue(() -> Spellbook.instance.getRenderer().destroy(this));
    }

    /**
     * Must be called from the rendering thread
     * Bind the texture to GL_TEXTURE_2D
     *
     * @param activeTextureUnit int 0-31 representing the texture unit to bind to
     */
    public void bind(int activeTextureUnit) {
        Spellbook.instance.getRenderer().bindTexture(this, activeTextureUnit);
    }

    /**
     * Must be called from the rendering thread
     * Unbinds the GL_TEXTURE_2D
     *
     * @param activeTextureUnit int 0-31 representing the texture unit to bind to
     */
    public void unbind(int activeTextureUnit) {
        Spellbook.instance.getRenderer().unbindTexture(activeTextureUnit);
    }

    /**
     * Used to load in TextureData
     *
     * @param width     The width of the texture
     * @param height    The height of the texture
     * @param textureId The OpenGL texture id
     */
    public record TextureInfo(int width, int height, int textureId) { }
}
