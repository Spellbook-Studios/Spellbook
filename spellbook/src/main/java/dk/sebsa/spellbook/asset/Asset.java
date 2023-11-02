package dk.sebsa.spellbook.asset;

import dk.sebsa.spellbook.asset.loading.AssetLocation;
import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Basic representation of loadable object for use in Spellbook
 * @author sebs
 * @since 1.0.0
 */
public abstract class Asset {
    @Getter
    protected AssetLocation location;
    @Getter
    private boolean isLoaded = false;
    @Getter
    protected int usages = 0;

    /**
     * Loads the asset into memory ready for usage within the engine
     * This might be called from a task so make sure to have some way to check if this is loaded
     */
    protected abstract void load();

    /**
     * Should remove all data related to the asset, and be ready for destruction
     */
    protected abstract void destroy();

    /**
     * Increase usages count & if not asset is not loaded it loads the asset
     * @return this
     */
    Asset get() {
        if(usages < 1) { load(); isLoaded = true; }
        usages++;
        return this;
    }

    /**
     * Should be done when asset is not needed to be loaded
     * If usages is 0 after unreferencing it, the asset is destroyed
     */
    public void unreference() {
        usages = usages - 1;
        if (usages < 1) {
            isLoaded = false;
            destroy();
        }
    }

    /**
     * Destroys the asset even if it is used by other objects
     */
    void forceDestroy() {
        destroy();
    }
}
