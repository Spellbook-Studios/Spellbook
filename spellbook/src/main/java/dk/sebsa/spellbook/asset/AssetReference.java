package dk.sebsa.spellbook.asset;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.TextAsset;
import lombok.Getter;

public class AssetReference {
    public final String location;
    public final LocationTypes locationType;
    public final String name;
    private Asset asset; // Null if asset is not loaded, otherwise the asset represented at the asset location
    @Getter private int usages = 0;

    /**
     * @param location The location identifier of the asset
     * @param locationType The type of the location
     */
    public AssetReference(String location, LocationTypes locationType) {
        this.location = location;
        this.locationType = locationType;

        this.name = location;
    }

    public <T extends Asset> T get() {
        usages = usages + 1;
        if(asset == null) {
            Spellbook.getLogger().trace("Create asset: " + asset);
            if(location.endsWith(".txt")) asset = new TextAsset();
            else {
                Spellbook.instance.getLogger().err("Failed to identify asset: " + name);
                return null;
            }

            asset.load(this);
        }

        return (T) asset;
    }

    /**
     * Should be done when asset is not needed to be loaded
     * If usages is 0 after unrefrencing it, the asset is destroyed
     * Asset should no longer be used and set to null, if it is cached
     */
    public void unRefrence() {
        Spellbook.getLogger().trace("Destroy asset: " + asset);
        usages = usages - 1;
        asset.destroy();
        asset = null;
    }

    /**
     * Destroys the action even if it is used by another object
     */
    public void forceDestroy() {
        usages = 0;
        if(asset != null) asset.destroy();
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

    public enum LocationTypes {
        Disk, Jar,
    }
}
