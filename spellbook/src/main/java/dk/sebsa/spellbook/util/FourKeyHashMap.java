package dk.sebsa.spellbook.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A hashmap with up to 4 key identifiers
 * @author sebsn
 * @since 0.0.1
 */
public class FourKeyHashMap<K1, K2, K3, K4, V> {
    private final Map<K1, Map<K2, Map<K3, Map<K4, V>>>> map; // The actual map. Holy jesus

    public FourKeyHashMap() {
        map = new HashMap<>();
    }

    /**
     * Get a value from the map using the 4 keys
     * @param key1
     * @param key2
     * @param key3
     * @param key4
     * @return The value identified by the 4 keys
     */
    public V get(K1 key1, K2 key2, K3 key3, K4 key4) {
        try {
            return map.get(key1).get(key2).get(key3).get(key4);
        } catch (NullPointerException e) { return null; }
    }

    /**
     * If a value corresponding to the 4 keys exists within the map it is returned
     * Else V is put into the map and is returned
     * @param key1
     * @param key2
     * @param key3
     * @param key4
     * @param v Computed if value is absent
     * @return
     */
    public V getPut(K1 key1, K2 key2, K3 key3, K4 key4, Supplier<V> v) {
        V r = get(key1, key2, key3, key4);

        if (r == null) {
            r = v.get();
            put(key1,key2,key3,key4,r);
        }

        return r;
    }

    /**
     * Puts a value into the map identified by the four keys
     * @param key1
     * @param key2
     * @param key3
     * @param key4
     * @param v The value to store
     */
    public void put(K1 key1, K2 key2, K3 key3, K4 key4, V v) {
        var map1 = map.computeIfAbsent(key1, k -> new HashMap<>());
        var map2 = map1.computeIfAbsent(key2, k -> new HashMap<>());
        var map3 = map2.computeIfAbsent(key3, k -> new HashMap<>());
        map3.put(key4, v);
    }
}
