package dk.sebsa.spellbook.asset;

import dk.sebsa.Spellbook;
import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.asset.loading.AssetLocation;
import dk.sebsa.spellbook.audio.Sound;
import dk.sebsa.spellbook.graphics.opengl.*;
import dk.sebsa.spellbook.marble.FontType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads and manages assets at runtime
 *
 * @author sebs
 * @since 1.0.0
 **/
public class AssetManager {
    private final Map<String, Asset> assets = new HashMap<>();
    private static AssetManager instance;

    /**
     * Creates and sets the instance of AssetManager
     */
    public AssetManager() {
        instance = this;
    }

    private Asset makeAsset(String location) {
        if (location.endsWith(".txt") || location.endsWith(".xml"))
            return new TextAsset();
        else if (location.endsWith(".glsl"))
            return new GLSLShaderProgram();
        else if (location.endsWith(".png"))
            return new Texture();
        else if (location.endsWith(".mat"))
            return new Material();
        else if (location.endsWith(".spr"))
            return new Sprite();
        else if (location.endsWith(".sht"))
            return new SpriteSheet();
        else if (location.endsWith(".ogg"))
            return new Sound();
        else if (location.endsWith(".ttf"))
            return new FontType();
        else {
            Spellbook.instance.error("Failed to identify asset type: " + location, false);
            return null;
        }
    }

    /**
     * Registers asset references directly
     *
     * @param references A list of new references to register
     */
    public void registerReferences(List<AssetLocation> references) {
        for (AssetLocation ref : references) {
            Asset asset = makeAsset(ref.location());
            if (asset == null) continue;

            asset.location = ref;
            assets.put(ref.identifier().toString(), asset);
        }
    }

    /**
     * Gets an asset from the registry
     *
     * @param identifier An identifier representing the asset
     * @return The asset
     */
    public Asset getAsset(Identifier identifier) {
        return assets.get(identifier.toString()).get();
    }


    /**
     * Gets an asset from the registry
     *
     * @param identifier An identifier representing the asset
     * @return The asset
     */
    public static Asset getAssetS(Identifier identifier) {
        return instance.getAsset(identifier);
    }

    /**
     * Destroys all assets, done at cleanup
     *
     * @param logger Main spellbook logger
     */
    public void engineCleanup(Logger logger) {
        logger.log("AssetManger cleanup, let's see the leaks");

        for (Asset asset : assets.values()) {
            if (asset.getUsages() > 0) {
                logger.warn("Asset was still referenced at cleanup, possible leak? " + asset.location.identifier());
                try {
                    asset.forceDestroy();
                } catch (Exception e) {
                    logger.err("Failed to destroy asset: " + asset.location.identifier());
                }
            }
        }
    }
}
