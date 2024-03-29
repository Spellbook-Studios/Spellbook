package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

/**
 * A sprite asset
 * A sprite has a texture, and it's offset (pos, size) on the texture, and padding (used for scaling)
 *
 * @author sebs
 * @since 1.0.0
 */
public class Sprite extends Asset {
    private final Rect uv = new Rect(0, 0, 1, 1);
    private final Rect paddingUV = new Rect(0, 0, 0, 0);
    @Getter
    private Rect offset;
    @Getter
    private Rect padding;
    @Getter
    private Material material;
    // Might exist if loaded with texture and not material directly
    private Texture texture;

    /**
     * Alternative Sprite Constructer
     *
     * @param offset   The offset
     * @param padding  The padding
     * @param material The material
     */
    public Sprite(Rect offset, Rect padding, Material material) {
        this.offset = offset;
        this.padding = padding;
        this.material = material;
    }

    /**
     * Normal sprite constructor used by AssetReference.java
     */
    public Sprite() {
    }

    @Override
    public void load() {
        try {
            List<String> file = FileUtils.readAllLinesList(FileUtils.loadFile(location));
            for (String line : file) {
                if (line.startsWith("t")) {
                    texture = (Texture) AssetManager.getAssetS(new Identifier(line.split(":", 2)[1]));
                    material = new Material(Color.white, texture);
                } else if (line.startsWith("o")) {
                    String[] e = line.split(":")[1].split(",");
                    offset = new Rect(Float.parseFloat(e[0]), Float.parseFloat(e[1]), Float.parseFloat(e[2]), Float.parseFloat(e[3]));
                } else if (line.startsWith("p")) {
                    String[] e = line.split(":")[1].split(",");
                    padding = new Rect(Float.parseFloat(e[0]), Float.parseFloat(e[1]), Float.parseFloat(e[2]), Float.parseFloat(e[3]));
                }
            }
        } catch (IOException e) {
            Spellbook.instance.error("Sprite: Failed to load file at location: " + location, false);
        }
    }

    @Override
    public void destroy() {
        if (texture != null) texture.unreference();
    }

    /**
     * @return The uv map of the sprite
     */
    public Rect getUV() {
        if (offset == null) return null;
        if (material.getTexture() == null) return uv;

        float w = material.getTexture().getWidth();
        float h = material.getTexture().getHeight();
        return uv.set(offset.x / w, offset.y / h, offset.width / w, offset.height / h);
    }

    /**
     * @return Returns the padding UV map
     */
    public Rect getPaddingUV() {
        if (padding == null) return null;
        if (material.getTexture() == null) return paddingUV;

        float w = material.getTexture().getWidth();
        float h = material.getTexture().getHeight();
        return paddingUV.set(padding.x / w, padding.y / h, padding.width / w, padding.height / h);
    }
}
