package dk.sebsa.spellbook.data;

import dk.sebsa.spellbook.asset.Identifier;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
@CustomLog
public class FileStore implements DataStore, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    final Map<String, Integer> integers;
    final Map<String, Float> floats;
    final Map<String, Double> doubles;
    final Map<String, Boolean> booleans;

    public static FileStore fromFile(File file) throws IOException, ClassNotFoundException {
        if (!file.exists()) return new FileStore(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        FileStore store = (FileStore) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return store;
    }

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
    public void getObject(Identifier identifier, Class<?> schema) {

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
    public void getOrDefaultObject(Identifier identifier, Supplier<Object> def, Class<?> schema) {

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
    public void storeObject(Identifier identifier, Object o) {

    }
}
