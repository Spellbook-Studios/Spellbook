package dk.sebsa.spellbook.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for creating files
 * @author sebsn
 * @since 0.0.1
 */
public class FileUtils {
    /**
     * Loads a file either on drive space or in jar resources
     * @param location The location of the file. Prefix with "/" when acssesing jar resources
     * @return An inputstream of the file
     * @throws IOException If file can't be found or loaded
     */
    public static InputStream loadFile(String location) throws IOException {
        try {
            if(location.startsWith("/")) {
                location = location.replaceFirst("/", "");
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

                return classLoader.getResourceAsStream(location);
            } else return new FileInputStream(location);
        } catch (Exception e) {
            throw new IOException("FileUtils, can't load file: " + location);
        }
    }

    /**
     * Lists alle files within a directory recursively
     *
     * @param folder The folder to search within
     * @return A list of all the files found
     */
    public static List<File> listFilesInFolder(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                files.addAll(listFilesInFolder(fileEntry));
            } else {
                files.add(fileEntry);
            }
        }

        return files;
    }
}
