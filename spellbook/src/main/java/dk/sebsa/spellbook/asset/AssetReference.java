package dk.sebsa.spellbook.asset;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.audio.Sound;
import dk.sebsa.spellbook.opengl.*;
import lombok.Getter;

/**
 * A reference to the asset
 * Can load and unload the asset at runtime
 *
 * @author sebs
 * @since 1.0.0
 */
public class AssetReference {
    /**
     * Location of the asset
     */
    public final String location;
    /**
     * The type of location
     */
    public final LocationTypes locationType;
    /**
     * The name / identifier of the asset
     */
    public final String name;
    private Asset asset; // Null if asset is not loaded, otherwise the asset represented at the asset location
    @Getter
    private int usages = 0;

    /**
     * The name will be the assets location
     *
     * @param location     The location identifier of the asset
     * @param locationType The type of the location
     */
    public AssetReference(String location, LocationTypes locationType) {
        this.location = location;
        this.locationType = locationType;
        this.name = location.replace('\\', '/'); // In case of windows
    }

    /***
     * @param location     The location identifier of the asset
     * @param name         The name and identifier of the asset
     * @param locationType The type of the location
     */
    public AssetReference(String location, String name, LocationTypes locationType) {
        this.location = location;
        this.locationType = locationType;
        this.name = name;
    }

    /**
     * Gets the instance of the asset (Loads the asset if it is the first use)
     *
     * @param <T> The type of asset
     * @return The asset cast to the type requested
     */
    public <T extends Asset> T get() {
        usages = usages + 1;
        if (asset == null) {
            if (location.endsWith(".txt")) asset = new TextAsset();
            else if (location.endsWith(".glsl")) asset = new GLSLShaderProgram();
            else if (location.endsWith(".png")) asset = new Texture();
            else if (location.endsWith(".mat")) asset = new Material();
            else if (location.endsWith(".spr")) asset = new Sprite();
            else if (location.endsWith(".sht")) asset = new SpriteSheet();
            else if (location.endsWith(".ogg")) asset = new Sound();
            else {
                Spellbook.instance.error("Failed to identify asset type: " + name, false);
                return null;
            }

            asset.load(this);
        }

        //noinspection unchecked
        return (T) asset;
    }

    /**
     * Should be done when asset is not needed to be loaded
     * If usages is 0 after unreferencing it, the asset is destroyed
     * Asset should no longer be used and set to null, if it is cached
     */
    public void unReference() {
        usages = usages - 1;
        if (usages > 0) return;
        asset.destroy();
        asset = null;
    }

    /**
     * Destroys the action even if it is used by another object
     */
    public void forceDestroy() {
        usages = 0;
        if (asset != null) asset.destroy();
    }

    @Override
    public String toString() {
        return "AssetReference{" +
                "location='" + location + '\'' +
                ", locationType=" + locationType +
                ", name='" + name + '\'' +
                ", usages=" + usages +
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
