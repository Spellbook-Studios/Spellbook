package dk.sebsa.spellbook.asset.loading;

/**
 * The location and identifier of an asset
 *
 * @param location The location of the asset
 * @param locationType The type of location
 * @param name The name / identifier of the asset
 * @author sebs
 * @since 1.0.0
 */
public record AssetLocation(String location, LocationTypes locationType, String name) {
    /**
     * The name is discerned from the location
     *
     * @param location The location of the asset
     * @param locationType The type of location
     */
    public AssetLocation(String location, LocationTypes locationType) {
        this(location, locationType, location.replace('\\', '/'));
    }

    @Override
    public String toString() {
        return "AssetLocation{" +
                "location='" + location + '\'' +
                ", locationType=" + locationType +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * The types of location
     *
     * @author sebs
     * @since 1.0.0
     */
    public enum LocationTypes {
        /**
         * The asset is located on a disk drive
         */
        Disk,
        /**
         * The asset is located on the jars classpath
         */
        Jar,
    }
}
