package dk.sebsa.spellbook.data;

import dk.sebsa.spellbook.asset.Identifier;
import lombok.CustomLog;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Load, Read & Change, Store data to a file
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class FileStore implements DataStore, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Map<String, Integer> integers;
    private final Map<String, Float> floats;
    private final Map<String, Double> doubles;
    private final Map<String, Boolean> booleans;
    private final Map<String, String> strings;
    private final Map<String, Object> objects;

    public FileStore() {
        this.integers = new HashMap<>();
        this.floats = new HashMap<>();
        this.doubles = new HashMap<>();
        this.booleans = new HashMap<>();
        this.strings = new HashMap<>();
        this.objects = new HashMap<>();
    }

    /**
     * Creates a filestore from a file
     *
     * @param file The file to load from
     * @return Filestore loaded from file
     * @throws IOException            If an IO Exception occurs
     * @throws ClassNotFoundException If the javaruntime cant find the class referenced in the file
     */
    public static FileStore fromFile(File file) throws IOException, ClassNotFoundException {
        if (!file.exists()) return new FileStore();
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        FileStore store = (FileStore) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return store;
    }

    /**
     * Stores a filestore to a file
     *
     * @param file      File to store to
     * @param fileStore Filestore to store
     * @throws IOException If an IO Exception occurs
     */
    public static void toFile(File file, FileStore fileStore) throws IOException {
        // Ensure the file exists
        file.getParentFile().mkdirs();
        file.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(fileStore);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    @Override
    public int getInt(Identifier identifier) {
        return integers.get(identifier.toString());
    }

    @Override
    public boolean getBool(Identifier identifier) {
        return booleans.get(identifier.toString());
    }

    @Override
    public float getFloat(Identifier identifier) {
        return floats.get(identifier.toString());
    }

    @Override
    public double getDouble(Identifier identifier) {
        return doubles.get(identifier.toString());
    }

    @Override
    public String getString(Identifier identifier) {
        return strings.get(identifier.toString());
    }

    @Override
    public <T extends Serializable> T getObject(Identifier identifier) {
        return (T) objects.get(identifier.toString());
    }

    @Override
    public int getOrDefaultInt(Identifier identifier, int def) {
        return integers.computeIfAbsent(identifier.toString(), (i) -> def);
    }

    @Override
    public boolean getOrDefaultBool(Identifier identifier, boolean def) {
        return booleans.computeIfAbsent(identifier.toString(), (i) -> def);
    }

    @Override
    public float getOrDefaultFloat(Identifier identifier, float def) {
        return floats.computeIfAbsent(identifier.toString(), (i) -> def);
    }

    @Override
    public double getOrDefaultDouble(Identifier identifier, double def) {
        return doubles.computeIfAbsent(identifier.toString(), (i) -> def);
    }

    @Override
    public String getOrDefaultString(Identifier identifier, String def) {
        return strings.computeIfAbsent(identifier.toString(), (i) -> def);
    }

    @Override
    public <T extends Serializable> T getOrDefaultObject(Identifier identifier, Supplier<Object> def) {
        return (T) objects.computeIfAbsent(identifier.toString(), (i) -> def.get());
    }

    @Override
    public void storeInt(Identifier identifier, int i) {
        integers.put(identifier.toString(), i);
    }

    @Override
    public void storeBool(Identifier identifier, boolean b) {
        booleans.put(identifier.toString(), b);
    }

    @Override
    public void storeFloat(Identifier identifier, float f) {
        floats.put(identifier.toString(), f);
    }

    @Override
    public void storeDouble(Identifier identifier, double d) {
        doubles.put(identifier.toString(), d);
    }

    @Override
    public void storeString(Identifier identifier, String s) {
        strings.put(identifier.toString(), s);
    }

    @Override
    public void storeObject(Identifier identifier, Object o) {
        objects.put(identifier.toString(), o);
    }
}
