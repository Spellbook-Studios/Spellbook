package dk.sebsa.spellbook.data;

import dk.sebsa.spellbook.asset.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileStoreTest {
    @TempDir
    File tempDir;

    @Test
    void testInt() throws IOException, ClassNotFoundException {
        FileStore store = new FileStore();
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
        FileStore store = new FileStore();
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
        FileStore store = new FileStore();
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
        FileStore store = new FileStore();
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

    @Test
    void testString() throws IOException, ClassNotFoundException {
        FileStore store = new FileStore();
        assertEquals("HELLO", store.getOrDefaultString(new Identifier("test", "1"), "HELLO"));
        assertEquals("HELLO", store.getOrDefaultString(new Identifier("test", "1"), "WORLD?"));
        assertEquals("HELLO", store.getString(new Identifier("test", "1")));

        store.storeString(new Identifier("test", "2"), "WORLD!");
        assertEquals("WORLD!", store.getString(new Identifier("test", "2")));

        // Test saving data to file
        FileStore.toFile(new File(tempDir, "test-string.data"), store);
        DataStore store2 = FileStore.fromFile(new File(tempDir, "test-string.data"));
        assertEquals("HELLO", store2.getOrDefaultString(new Identifier("test", "1"), "WORLD?"));
        assertEquals("HELLO", store2.getString(new Identifier("test", "1")));
        assertEquals("WORLD!", store2.getString(new Identifier("test", "2")));
    }

    @Test
    void testObject() throws IOException, ClassNotFoundException {
        TestData data = new TestData("HELLO", 24.5f);
        TestData data1 = new TestData("WORLD?", 420.69f);
        TestData data2 = new TestData("WORLD!", 2.5f);

        FileStore store = new FileStore();
        assertEquals(data, store.getOrDefaultObject(new Identifier("test", "1"), () -> data));
        assertEquals(data, store.getOrDefaultObject(new Identifier("test", "1"), () -> data1));
        assertEquals(data, store.getObject(new Identifier("test", "1")));

        store.storeObject(new Identifier("test", "2"), data2);
        assertEquals(data2, store.getObject(new Identifier("test", "2")));

        // Test saving data to file
        FileStore.toFile(new File(tempDir, "test-object.data"), store);
        DataStore store2 = FileStore.fromFile(new File(tempDir, "test-object.data"));
        assertEquals(data, store2.getOrDefaultObject(new Identifier("test", "1"), () -> data1));
        assertEquals(data, store2.getObject(new Identifier("test", "1")));
        assertEquals(data2, store2.getObject(new Identifier("test", "2")));
    }
}