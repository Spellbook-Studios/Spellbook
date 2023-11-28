package dk.sebsa.spellbook.saves;

import dk.sebsa.spellbook.asset.Identifier;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.CustomLog;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

/**
 * Functions for retriving and storing data in the current users local app data directory
 * <p>
 * On Mac OS X : /Users/<Account>/Library/Application Support/Spellbook-<namespace>
 * On Windows XP : C:\Documents and Settings\<Account>\Application Data\Local Settings\Spellbook-Data\Spellbook-<namespace>
 * On Windows 7 : C:\Users\<Account>\AppData\Spellbook-Data\Spellbook-<namespace>
 * On Unix/Linux : /home/<Account>/.local/share/Spellbook-<namespace>
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class SaveDataUtils {
    private static final AppDirs APP_DIRS = AppDirsFactory.getInstance();
    private static HashMap<String, SaveData> saveDataMap = new HashMap<>();

    private static void ensureFile(Identifier identifier) {
        if (!saveDataMap.containsKey(identifier.getNamespace())) {
            logger.log("Loading savedata file for namespace: " + identifier.getNamespace());
            String appDir = APP_DIRS.getUserDataDir("Spellbook" + identifier.getNamespace(), null, "Spellbook-Data");
            logger.log("Savedata dir: " + appDir);

            File dataFile = new File(appDir + File.separatorChar + "savedata");
            if (!dataFile.exists()) {
                saveDataMap.put(identifier.getNamespace(), new SaveData());
            } else {
                try {
                    saveDataMap.put(identifier.getNamespace(), SaveData.fromFile(dataFile));
                } catch (IOException e) {
                    logger.err("An IO excpetion occured whilst loading savedata file: " + logger.stackTrace(e));
                    saveDataMap.put(identifier.getNamespace(), new SaveData());
                }
            }
        }
    }

    /**
     * Gets an int from the savedata config file, and if the value is not presents puts the provided default value there instead
     *
     * @param identifier Identifier of the variable
     * @param def        The default value to return if the data is not found
     * @return The variable with the given identifier
     */
    public static int getIntOrDefault(Identifier identifier, int def) {
        ensureFile(identifier);
        return saveDataMap.get(identifier.getNamespace()).ints.computeIfAbsent(identifier.toString(), (s) -> def);
    }

    /**
     * Gets a float from the savedata config file, and if the value is not presents puts the provided default value there instead
     *
     * @param identifier Identifier of the variable
     * @param def        The default value to return if the data is not found
     * @return The variable with the given identifier
     */
    public static float getFloatOrDefault(Identifier identifier, float def) {
        ensureFile(identifier);
        return saveDataMap.get(identifier.getNamespace()).floats.computeIfAbsent(identifier.toString(), (s) -> def);
    }

    /**
     * Gets a boolean from the savedata config file, and if the value is not presents puts the provided default value there instead
     *
     * @param identifier Identifier of the variable
     * @param def        The default value to return if the data is not found
     * @return The variable with the given identifier
     */
    public static boolean getBoolOrDefault(Identifier identifier, boolean def) {
        ensureFile(identifier);
        return saveDataMap.get(identifier.getNamespace()).bools.computeIfAbsent(identifier.toString(), (s) -> def);
    }

    /**
     * Gets a string from the savedata config file, and if the value is not presents puts the provided default value there instead
     *
     * @param identifier Identifier of the variable
     * @param def        The default value to return if the data is not found
     * @return The variable with the given identifier
     */
    public static String getStringOrDefault(Identifier identifier, String def) {
        ensureFile(identifier);
        return saveDataMap.get(identifier.getNamespace()).strings.computeIfAbsent(identifier.toString(), (s) -> def);
    }

    /**
     * Saves all loaded data to disk
     */
    public static void saveData() {
        for (String namespace : saveDataMap.keySet()) {
            String appDir = APP_DIRS.getUserDataDir("Spellbook" + namespace, null, "Spellbook-Data");
            File dataFile = new File(appDir + File.separatorChar + "savedata");
            logger.trace("Saving datafile: " + dataFile.toString());
            try {
                SaveData.toFile(dataFile, saveDataMap.get(namespace));
            } catch (IOException e) {
                logger.err("Failed to save data: " + logger.stackTrace(e));
            }
        }
    }

    /**
     * Represents stored savedata
     */
    public static class SaveData {
        HashMap<String, Integer> ints = new HashMap<>();
        HashMap<String, Boolean> bools = new HashMap<>();
        HashMap<String, Float> floats = new HashMap<>();
        HashMap<String, String> strings = new HashMap<>();

        /**
         * Loads savedata from a file
         *
         * @param dataFile File on disk that contains the savedata
         * @return The savedata
         * @throws IOException If the file is not found, or there occurs an error whilst reading the file
         */
        public static SaveData fromFile(File dataFile) throws IOException {
            SaveData data = new SaveData();
            List<String> lines = FileUtils.readAllLinesList(Base64.getDecoder().wrap(new FileInputStream(dataFile)));

            for (String l : lines) {
                String[] idTypeValueSplit = l.split("\\?", 3);
                if (idTypeValueSplit.length < 3) continue;

                String id = idTypeValueSplit[0];
                String type = idTypeValueSplit[1];
                String value = idTypeValueSplit[2];

                switch (type) {
                    case "i" -> data.ints.put(id, Integer.parseInt(value));
                    case "f" -> data.floats.put(id, Float.parseFloat(value));
                    case "b" -> data.bools.put(id, value.equalsIgnoreCase("t"));
                    case "s" -> data.strings.put(id, value);
                    case null, default -> logger.warn("Could not parse savedata line: " + l);
                }
            }

            return data;
        }

        /**
         * Saves data from SaveData to a file, so it can be loaded again
         *
         * @param dataFile File to save to
         * @param saveData Data to save
         * @throws IOException if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
         */
        public static void toFile(File dataFile, SaveData saveData) throws IOException {
            // Create File
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();

            // Save data
            StringBuilder sb = new StringBuilder();
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, false));

            for (String k : saveData.ints.keySet()) {
                sb.append(k).append("?").append("i").append("?").append(saveData.ints.get(k)).append("\n");
            }
            for (String k : saveData.floats.keySet()) {
                sb.append(k).append("?").append("f").append("?").append(saveData.floats.get(k)).append("\n");
            }
            for (String k : saveData.bools.keySet()) {
                sb.append(k).append("?").append("b").append("?").append(saveData.bools.get(k) ? "t" : "f").append("\n");
            }
            for (String k : saveData.strings.keySet()) {
                sb.append(k).append("?").append("s").append("?").append(saveData.strings.get(k)).append("\n");
            }

            writer.write(Base64.getEncoder().encodeToString(sb.toString().getBytes(StandardCharsets.UTF_8)));
            writer.close();
        }
    }
}
