package dk.sebsa.spellbook.core;

import dk.sebsa.Spellbook;
import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.mana.LogFormatter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dk.sebsa.spellbook.util.FileUtils.zipSingleFile;

public class StoredLogger extends SpellbookLogger {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-MM-u");

    private final boolean zipped;
    private final File target;

    private FileWriter fw;

    public StoredLogger(LogFormatter formatterIn, SpellbookCapabilities caps) {
        super(formatterIn, caps);

        zipped = caps.logStorageMode.equals(SpellbookCapabilities.LogStorageModes.zipped);
        target = new File(caps.logStoreTarget);

        // Create log targets
        try {
            if(!target.getParentFile().mkdirs() && !target.createNewFile()) { // If file exits
                target.delete();
                target.createNewFile();
            }

            fw = new FileWriter(target, false);
            fw.write("# Logs from SpellbookLogger with Mana\n");
        } catch (IOException e) { super.print("Failed to create log file: " + new ClassLogger(this, null).stackTrace(e)); }
    }

    @Override
    protected void print(String s) {
        super.print(s);

        try {
            fw.append(s).append("\n");
        } catch (IOException e) {  /* There's nothing we can do to log this, and nothing we can do to fix this */ }
    }

    /**
     * Gets the number of files in a directory which filename starts with [start]
     * @param start The prefix to search for
     * @param parent The directory to search in
     * @return The number of files +1
     */
    public static int getNumber(String start, String parent) {
        File directory = new File(parent);
        String[] files = directory.list();
        int i = 1;
        for(String s : files) {
            if(s.startsWith(start)) i++;
        }
        return i;
    }

    @SneakyThrows @Override
    public void close() {
        super.close();

        fw.close();
        if(zipped) {
            String parent = target.getParent();
            try {
                String zipName = "log-"+dtf.format(LocalDateTime.now());
                zipName += "-"+getNumber(zipName, parent);

                zipSingleFile(Paths.get(target.getPath()), parent + "/" + zipName + ".zip");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
