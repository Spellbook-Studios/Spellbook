package dk.sebsa.spellbook.asset.loading;

import dk.sebsa.spellbook.util.FileUtils;
import lombok.CustomLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads assets from a folder
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class FolderAssetProvider extends AssetProvider {
    private final File folder;
    private final String namePrefix;

    /**
     * @param folder     The folder to search files from (searches recursively)
     * @param namePrefix Prefix before assets names
     *                   Lets say assets are located in ../idk/the/path/assets/
     *                   An asset located at
     *                   ../idk/the/path/assets/textures/hello.png
     *                   becomes [namePrefix]/textures/hello.png
     */
    public FolderAssetProvider(File folder, String namePrefix) {
        this.folder = folder;
        this.namePrefix = namePrefix;
    }

    @Override
    public List<AssetLocation> getAssets() {
        List<AssetLocation> assets = new ArrayList<>();

        if (!folder.isDirectory()) {
            logger.warn("FolderAsssetProvider: Path is not a directory: " + folder.getPath());
            return assets;
        }

        // For naming assets
        for (File f : FileUtils.listFilesInFolder(folder)) {
            String assetName = f.getPath()
                    .replace('\\', '/')
                    .replace(folder.getPath().replace('\\', '/'), namePrefix);
            assets.add(new AssetLocation(f.getPath(), AssetLocation.LocationTypes.Disk, assetName));
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
