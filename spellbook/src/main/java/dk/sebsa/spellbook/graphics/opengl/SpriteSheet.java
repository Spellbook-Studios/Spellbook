package dk.sebsa.spellbook.graphics.opengl;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.math.Rect;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A spritesheet asset
 * A sprite sheet contains multiple sprites which are loaded in together with the spritesheet
 *
 * @author sebs
 * @since 1.0.0
 */
public class SpriteSheet extends Asset {
    @Getter
    private final List<Sprite> sprites = new ArrayList<>();
    private final Map<String, Sprite> spriteMap = new HashMap<>();

    private Material material;

    // TEMP VARS
    private Rect offset, padding;
    private String s;

    @Override
    public void load() {
        try {
            List<String> raw = FileUtils.readAllLinesList(FileUtils.loadFile(location));
            for (String line : raw) {
                if (line.startsWith("m")) {
                    material = (Material) AssetManager.getAssetS(new Identifier(line.split(":", 2)[1]));
                } else if (line.startsWith("o")) {
                    String[] e = line.split(":")[1].split(",");
                    offset = new Rect(Float.parseFloat(e[0]), Float.parseFloat(e[1]), Float.parseFloat(e[2]), Float.parseFloat(e[3]));
                } else if (line.startsWith("p")) {
                    String[] e = line.split(":")[1].split(",");
                    padding = new Rect(Float.parseFloat(e[0]), Float.parseFloat(e[1]), Float.parseFloat(e[2]), Float.parseFloat(e[3]));
                } else if (line.startsWith("n")) s = line.split(":")[1];
                create();
            }
        } catch (IOException e) {
            Spellbook.instance.error("SpriteSheet: Failed to load file at location: " + location, false);
        }
    }

    private void create() {
        if (offset != null && padding != null && s != null) {
            sprites.addFirst(new Sprite(offset, padding, material));
            spriteMap.put(s, sprites.getFirst());

            offset = null;
            s = null;
            padding = null;
        }
    }

    @Override
    public void destroy() {
        material.unreference();
    }

    /**
     * Gets a sprite from the spritesheet
     * You don't need to manually unreference the sprites, they are destroyed together with the sheet
     *
     * @param name Name of the sprite
     * @return The sprite with the name specified, or null if non-existent
     */
    public Sprite spr(String name) {
        return spriteMap.get(name);
    }

    @Override
    public String toString() {
        return "SpriteSheet{" +
                "location=" + location +
                ", material=" + material +
                ", offset=" + offset +
                ", padding=" + padding +
                '}';
    }
}
