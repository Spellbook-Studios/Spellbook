package dk.sebsa.spellbook.asset;

/**
 * Basic representation of lodable object for use in Spellbook
 * @author sebs
 * @since 1.0.0
 */
public abstract class Asset {
    public boolean isLoaded = false;
    /**
     * Loads the asset into memory ready for usage within the engine
     * This might be called from a task so make sure to have some way to check if this is loaded
     *
     * @param location The location of the asset, used for loading data from files
     */
    public abstract void load(AssetReference location);

    /**
     * Should remove all data related to the asset, and be ready for destruction
     */
    public abstract void destroy();
}
