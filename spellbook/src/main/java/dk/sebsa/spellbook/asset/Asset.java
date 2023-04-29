package dk.sebsa.spellbook.asset;

/**
 * Basic representation of lodable object for use in Spellbook
 * @author sebs
 * @since 1.0.0
 */
public interface Asset {
    /**
     * Loads the asset into memory ready for usage within the engine
     *
     * @param location The location of the asset, used for loading data from files
     */
    void load(AssetReference location);

    /**
     * Should remove all data related to the asset, and be ready for destruction
     */
    void destroy();
}
