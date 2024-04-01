package dk.sebsa.spellbook.asset.loading;

import dk.sebsa.Spellbook;
import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.ClassLogger;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads assets from a folder
 *
 * @author sebs
 * @since 1.0.0
 */
@ToString
public class FolderAssetProvider extends AssetProvider {
    @ToString.Include
    private final File folder;
    @ToString.Include
    private final String namespace;
    private Logger logger;

    /**
     * @param folder    The folder to search files from (searches recursively)
     * @param namespace The namespace of the assets that are loaded
     */
    public FolderAssetProvider(File folder, String namespace) {
        this.folder = folder;
        this.namespace = namespace;
    }

    @Override
    public List<AssetLocation> getAssets() {
        logger = new ClassLogger(getClass(), Spellbook.getLogger()); // Yee, so when using @CustomLog, the mainlogger would be null since Spellbook is initialized when assetproviders are
        List<AssetLocation> assets = new ArrayList<>();

        if (!folder.exists()) {
            logger.warn("FolderAsssetProvider: Path does not exist: " + folder.getPath());
            return assets;
        }
        if (!folder.isDirectory()) {
            logger.warn("FolderAsssetProvider: Path is not a directory: " + folder.getPath());
            return assets;
        }

        // For naming assets
        for (File f : FileUtils.listFilesInFolder(folder)) {
            String assetName = f.getPath()
                    .replace(folder.getPath(), "")
                    .replace('\\', '/');
            if (assetName.charAt(0) == '/')
                assetName = assetName.replaceFirst("/", "");

            assets.add(new AssetLocation(new Identifier(namespace, assetName), f.getPath(), AssetLocation.LocationTypes.Disk));
        }

        return assets;
    }
}
