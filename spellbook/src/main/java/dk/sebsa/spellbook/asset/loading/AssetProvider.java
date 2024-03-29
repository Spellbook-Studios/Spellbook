package dk.sebsa.spellbook.asset.loading;

import java.util.List;

/**
 * Provides a list of asset refrences to the assetmanager at runtime
 *
 * @since 1.0.0
 * @author sebs
 **/
public abstract class AssetProvider {
    /**
     * Provides a list of assets stored in the asset location
     *
     * @return The assets
     */
    public abstract List<AssetLocation> getAssets();
}
