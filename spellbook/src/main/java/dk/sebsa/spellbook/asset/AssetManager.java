package dk.sebsa.spellbook.asset;

import dk.sebsa.mana.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads and manages assets at runtime
 * 
 * @since 0.0.1
 * @author sebs
 **/
public class AssetManager {
    private final Map<String, AssetReference> assets = new HashMap<>();
    private static AssetManager instance;

    public AssetManager() {
        instance = this;
    }

    /**
     * Registers asset references directly
     * @param references A list of new references to register
     */
    public void registerReferences(List<AssetReference> references) {
        for(AssetReference ref : references) {
            assets.put(ref.name, ref);
        }
    }

    /**
     * Gets an asset refrence from assetmanagers registry
     * @param name The name of the asset
     * @return The asset
     */
    public AssetReference getAsset(String name) {
        return assets.get(name);
    }


    /**
     * Gets an asset refrence from assetmanagers registry
     * @param name The name of the asset
     * @return The asset
     */
    public static AssetReference getAssetS(String name) {
        return instance.getAsset(name);
    }

    /**
     * Destroys all assets, done at cleanup
     */
    public void engineCleanup(Logger logger) {
        logger.log("AssetManger cleanup, let's see the leaks");

        for(AssetReference asset : assets.values()) {
            if(asset.getUsages() > 0) logger.warn("Asset was still refrenced at cleanup, possible leak? " + asset);
            try {
                asset.forceDestroy();
            } catch (Exception e) { logger.err("Failed to destroy asset: " + asset); }
        }
    }
}
