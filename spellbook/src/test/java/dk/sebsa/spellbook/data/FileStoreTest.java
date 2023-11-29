package dk.sebsa.spellbook.data;

import dk.sebsa.spellbook.asset.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FileStoreTest {
    @TempDir
    File tempDir;

    @Test
    void testInt() throws IOException, ClassNotFoundException {
        FileStore store = new FileStore(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        assertEquals(518, store.getOrDefaultInt(new Identifier("test", "1"), 518));
        assertEquals(518, store.getOrDefaultInt(new Identifier("test", "1"), -4));
        assertEquals(518, store.getInt(new Identifier("test", "1")));

        store.storeInt(new Identifier("test", "2"), 200);
        assertEquals(200, store.getInt(new Identifier("test", "2")));

        // Test saving data to file
        FileStore.toFile(new File(tempDir, "test-int.data"), store);
        DataStore store2 = FileStore.fromFile(new File(tempDir, "test-int.data"));
        assertEquals(518, store2.getOrDefaultInt(new Identifier("test", "1"), -4));
        assertEquals(518, store2.getInt(new Identifier("test", "1")));
        assertEquals(200, store2.getInt(new Identifier("test", "2")));
    }

    @Test
    void testBool() throws IOException, ClassNotFoundException {
        FileStore store = new FileStore(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        assertTrue(store.getOrDefaultBool(new Identifier("test", "1"), true));
        assertTrue(store.getOrDefaultBool(new Identifier("test", "1"), false));
        assertTrue(store.getBool(new Identifier("test", "1")));

        store.storeBool(new Identifier("test", "2"), false);
        assertFalse(store.getBool(new Identifier("test", "2")));

        // Test saving data to file
        FileStore.toFile(new File(tempDir, "test-bool.data"), store);
        DataStore store2 = FileStore.fromFile(new File(tempDir, "test-bool.data"));
        assertTrue(store2.getOrDefaultBool(new Identifier("test", "1"), false));
        assertTrue(store2.getBool(new Identifier("test", "1")));
        assertFalse(store2.getBool(new Identifier("test", "2")));
    }

    @Test
    void testFloat() throws IOException, ClassNotFoundException {
        FileStore store = new FileStore(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        assertEquals(6.9f, store.getOrDefaultFloat(new Identifier("test", "1"), 6.9f));
        assertEquals(6.9f, store.getOrDefaultFloat(new Identifier("test", "1"), -4));
        assertEquals(6.9f, store.getFloat(new Identifier("test", "1")));

        store.storeFloat(new Identifier("test", "2"), 420.42f);
        assertEquals(420.42f, store.getFloat(new Identifier("test", "2")));

        // Test saving data to file
        FileStore.toFile(new File(tempDir, "test-float.data"), store);
        DataStore store2 = FileStore.fromFile(new File(tempDir, "test-float.data"));
        assertEquals(6.9f, store2.getOrDefaultFloat(new Identifier("test", "1"), -4));
        assertEquals(6.9f, store2.getFloat(new Identifier("test", "1")));
        assertEquals(420.42f, store2.getFloat(new Identifier("test", "2")));
    }

    @Test
    void testDouble() throws IOException, ClassNotFoundException {
        FileStore store = new FileStore(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        assertEquals(6.9d, store.getOrDefaultDouble(new Identifier("test", "1"), 6.9d));
        assertEquals(6.9d, store.getOrDefaultDouble(new Identifier("test", "1"), -4));
        assertEquals(6.9d, store.getDouble(new Identifier("test", "1")));

        store.storeDouble(new Identifier("test", "2"), 420.42d);
        assertEquals(420.42d, store.getDouble(new Identifier("test", "2")));

        // Test saving data to file
        FileStore.toFile(new File(tempDir, "test-double.data"), store);
        DataStore store2 = FileStore.fromFile(new File(tempDir, "test-double.data"));
        assertEquals(6.9d, store2.getOrDefaultDouble(new Identifier("test", "1"), -4));
        assertEquals(6.9d, store2.getDouble(new Identifier("test", "1")));
        assertEquals(420.42d, store2.getDouble(new Identifier("test", "2")));
    }
}