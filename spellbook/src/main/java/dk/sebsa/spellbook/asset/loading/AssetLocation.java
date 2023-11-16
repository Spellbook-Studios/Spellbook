package dk.sebsa.spellbook.asset.loading;

import dk.sebsa.spellbook.asset.Identifier;

/**
 * The location and identifier of an asset
 *
 * @param identifier   The unique asset identifier
 * @param location     The location of the asset
 * @param locationType The type of location
 * @author sebs
 * @since 1.0.0
 */
public record AssetLocation(Identifier identifier, String location, LocationTypes locationType) {
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

    @Override
    public String toString() {
        return "AssetLocation{" +
                "identifier=" + identifier +
                ", location='" + location + '\'' +
                ", locationType=" + locationType +
                '}';
    }
}
