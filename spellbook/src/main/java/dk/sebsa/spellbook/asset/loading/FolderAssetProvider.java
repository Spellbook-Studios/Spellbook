package dk.sebsa.spellbook.asset.loading;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads assets from a folder
 *
 * @author sebs
 * @since 1.0.0
 */
public class FolderAssetProvider extends AssetProvider {
    private final File folder;

    /**
     * @param folder The folder to search files from (searches recursively)
     */
    public FolderAssetProvider(File folder) {
        this.folder = folder;
        if(!folder.isDirectory()) {
            Spellbook.instance.error("Path is not a directory: " + folder.getPath(), true);
        }
    }

    @Override
    public List<AssetReference> getAssets() {
        List<AssetReference> assets = new ArrayList<>();

        for(File f : FileUtils.listFilesInFolder(folder)) {
            assets.add(new AssetReference(f.getPath(), AssetReference.LocationTypes.Disk));
        }

        return assets;
    }

    @Override
    public String toString() {
        return "FolderAssetProvider{" +
                "folder=" + folder.getPath() +
                '}';
    }
}
