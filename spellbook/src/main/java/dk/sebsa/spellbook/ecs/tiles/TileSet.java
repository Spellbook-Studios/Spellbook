package dk.sebsa.spellbook.ecs.tiles;

import dk.sebsa.spellbook.asset.AssetManager;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.graphics.opengl.Material;
import dk.sebsa.spellbook.graphics.opengl.Sprite;
import dk.sebsa.spellbook.graphics.opengl.SpriteSheet;
import dk.sebsa.spellbook.math.Vector2f;
import lombok.CustomLog;
import lombok.Getter;

/**
 * A map sprites (from the same spritesheet) that can have different attributes and are indexed by ints
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class TileSet {
    private final SpriteSheet spriteSheet;
    private final Sprite[] sprites;
    @Getter
    private final Material material;
    @Getter
    private final Vector2f tileSize = new Vector2f();

    private TileSet(SpriteSheet sht) {
        spriteSheet = sht;

        var source = sht.getSprites();
        sprites = new Sprite[source.size()];

        for (int i = 0; i < source.size(); i++) {
            sprites[i] = source.get(i);
        }

        material = source.getFirst().getMaterial();
        tileSize.set(source.getFirst().getOffset().width, source.getFirst().getOffset().height);
    }

    /**
     * Creates a new tileset from a spritesheet
     *
     * @param i Identifier of a spritesheet
     * @return A new tileset or null if the spritesheet wasn't found
     */
    public static TileSet fromSpriteSheet(Identifier i) {
        SpriteSheet s = (SpriteSheet) AssetManager.getAssetS(i);
        if (s == null) {
            logger.err("Failed to find spritesheet asset: " + i);
            return null;
        }

        return new TileSet(s);
    }

    /**
     * Gets the sprite at a specific tile
     *
     * @param i Tile to search for
     * @return A sprite or null, if i is out of bounds.
     */
    public Sprite spr(int i) {
        return sprites[i];
    }

    public void destroy() {
        spriteSheet.unreference();
    }
}
