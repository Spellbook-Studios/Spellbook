package dk.sebsa.spellbook.util;

import dk.sebsa.spellbook.asset.loading.AssetLocation;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utilities for creating files
 *
 * @author sebs
 * @since 1.0.0
 */
public class FileUtils {
    /**
     * Loads a file either on drive space or in jar resources
     *
     * @param location The location of the file.
     * @return An inputstream of the file
     * @throws IOException If file can't be found or loaded
     */
    public static InputStream loadFile(AssetLocation location) throws IOException {
        if (location.locationType() == AssetLocation.LocationTypes.Jar) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return classLoader.getResourceAsStream(location.location());
        } else {
            return new FileInputStream(location.location());
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

        File[] a = folder.listFiles();
        if (a == null) return files;

        for (final File fileEntry : a) {
            if (fileEntry.isDirectory()) {
                files.addAll(listFilesInFolder(fileEntry));
            } else {
                files.add(fileEntry);
            }
        }

        return files;
    }

    /**
     * Add all lines from file to list
     *
     * @param is An input stream for the file
     * @return A list of lines, does not end with a line ending charater
     * @throws IOException If the buffered reader erros
     */
    public static List<String> readAllLinesList(InputStream is) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        br.close();

        return list;
    }

    /**
     * Get all content from a file / InputStream as a string
     *
     * @param is An input stream for the file
     * @return A string representing all the content in the file with line endings
     * @throws IOException If the buffered reader errors
     */
    public static String readAllLines(InputStream is) throws IOException {
        StringBuilder e = new StringBuilder();
        for (String line : readAllLinesList(is)) {
            e.append(line).append("\n");
        }

        return e.toString();
    }

    /**
     * Zips a single file into a new zip file
     *
     * @param source      The file to zip
     * @param zipFileName The name of the zipfile
     * @throws IOException If the file fails to load or write
     */
    public static void zipSingleFile(Path source, String zipFileName) throws IOException {
        try (
                ZipOutputStream zos = new ZipOutputStream(
                        new FileOutputStream(zipFileName));
                FileInputStream fis = new FileInputStream(source.toFile())
        ) {

            ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
        }
    }

    /**
     * Reads an inputstream and stores the contents in a bytebuffer
     *
     * @param is         The input stream
     * @param bufferSize Buffer size
     * @return The buffer with the data
     * @throws IOException If the RBC fails to read from the input stream
     */
    public static ByteBuffer isToBB(InputStream is, int bufferSize) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(is);
        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferSize);

        while (true) {
            int bytes = rbc.read(buffer);
            if (bytes == -1) {
                break;
            }
            if (buffer.remaining() == 0) {
                buffer = resizeBuffer(buffer, buffer.capacity() * 2);
            }
        }
        buffer.flip();

        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
