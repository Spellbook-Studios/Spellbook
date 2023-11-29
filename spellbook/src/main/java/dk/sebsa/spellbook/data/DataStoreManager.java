package dk.sebsa.spellbook.data;

import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.util.PathUtils;
import lombok.CustomLog;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Help you manage datastore
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class DataStoreManager {
    private static final PathUtils PATH_UTILS = PathUtils.getInstance();
    private static final Map<String, FileStore> fileStores = new HashMap<>();

    /**
     * Get the filestore associated with an app and an identifier
     * The filestores data will automatically be saved to disk upon spellbooks shutdown
     *
     * @param app        App the data belongs to
     * @param identifier The identifier for the filestore
     * @return The datastore loaded from the file, or a new empty one if no file was found
     */
    public static DataStore getFileStore(Application app, Identifier identifier) {
        String p = PATH_UTILS.getUserLocalDataDir(app.name(), identifier.getNamespace()) + File.separatorChar + identifier.getPath() + ".dat";
        return fileStores.computeIfAbsent(p, (f) -> {
            try {
                return FileStore.fromFile(new File(f));
            } catch (IOException e) {
                logger.err("IOException whilst parsing DataStore file: " + logger.stackTrace(e));
                return new FileStore(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
            } catch (ClassNotFoundException e) {
                logger.err("ClassNotFoundException whilst parsing DataStore file: " + logger.stackTrace(e));
                return new FileStore(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
            }
        });
    }

    /**
     * Saves data to disk
     */
    public static void flushData() {
        for (String p : fileStores.keySet()) {
            try {
                logger.trace("Storing FileStore: " + p);
                FileStore.toFile(new File(p), fileStores.get(p));
            } catch (IOException e) {
                logger.err("Failed to save data for file: " + p);
            }
        }
    }
}
