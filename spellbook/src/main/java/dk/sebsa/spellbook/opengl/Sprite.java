package dk.sebsa.spellbook.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class Sprite implements Asset {
    @Getter private Rect offset;
    @Getter private Rect padding;
    @Getter private Material material;

    // Might exist if loaded with texture and not material directly
    private AssetReference textureR;
    private Texture texture;

    @Override
    public void load(AssetReference location) {
        try {
            List<String> file = FileUtils.readAllLinesList(FileUtils.loadFile(location.location));
            for(String line : file) {
                if(line.startsWith("t")) {
                    textureR = AssetManager.getAssetS(line.split(":")[1]);
                    texture = textureR.get();
                    material = new Material(Color.white, texture);
                } else if(line.startsWith("o")) {
                    String[] e = line.split(":")[1].split(",");
                    offset = new Rect(Float.parseFloat(e[0]),Float.parseFloat(e[1]),Float.parseFloat(e[2]),Float.parseFloat(e[3]));
                } else if(line.startsWith("p")) {
                    String[] e = line.split(":")[1].split(",");
                    padding = new Rect(Float.parseFloat(e[0]),Float.parseFloat(e[1]),Float.parseFloat(e[2]),Float.parseFloat(e[3]));
                }
            }
        } catch (IOException e) {
            Spellbook.instance.error("Sprite: Failed to load file at location: " + location, false);
        }
    }

    @Override
    public void destroy() {
        texture = null;
        if(textureR != null) textureR.unRefrence();
    }

    private final Rect uv = new Rect(0,0,0,0);
    public Rect getUV() {
        if(offset == null) return null;
        if(material.getTexture() == null) return uv;

        float w = material.getTexture().getWidth();
        float h = material.getTexture().getHeight();
        return uv.set(offset.x / w, offset.y / h, offset.width / w, offset.height / h);
    }

    private final Rect paddingUV = new Rect(0,0,0,0);
    public Rect getPaddingUV() {
        if(padding == null) return null;
        if(material.getTexture() == null) return paddingUV;

        float w = material.getTexture().getWidth();
        float h = material.getTexture().getHeight();
        return paddingUV.set(padding.x / w, padding.y / h, padding.width / w, padding.height / h);
    }
}
