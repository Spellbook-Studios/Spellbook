package dk.sebsa.spellbook.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A hashmap with up to 3 key identifiers
 *
 * @param <K1> Type of first key
 * @param <K2> Type of second key
 * @param <K3> Type of third key
 * @param <V>  Type of value
 * @author sebsn
 * @since 1.0.0
 */
public class ThreeKeyHashMap<K1, K2, K3, V> {
    private final Map<K1, Map<K2, Map<K3, V>>> map; // The actual map. Holy jesus

    /**
     * A hashmap with up to 4 key identifiers
     */
    public ThreeKeyHashMap() {
        map = new HashMap<>();
    }

    /**
     * Get a value from the map using the 4 keys
     *
     * @param key1 Key 1
     * @param key2 Key 2
     * @param key3 Key 3
     * @return The value identified by the 4 keys
     */
    public V get(K1 key1, K2 key2, K3 key3) {
        try {
            return map.get(key1).get(key2).get(key3);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * If a value corresponding to the 4 keys exists within the map it is returned
     * Else V is put into the map and is returned
     *
     * @param key1 Key 1
     * @param key2 Key 2
     * @param key3 Key 3
     * @param v    Computed if value is absent
     * @return Either the value mapped to the keys or if non existent the one supplied by v
     */
    public V getPut(K1 key1, K2 key2, K3 key3, Supplier<V> v) {
        V r = get(key1, key2, key3);

        if (r == null) {
            r = v.get();
            put(key1, key2, key3, r);
        }

        return r;
    }

    /**
     * Puts a value into the map identified by the four keys
     *
     * @param key1 Key 1
     * @param key2 Key 2
     * @param key3 Key 3
     * @param v    The value to store
     */
    public void put(K1 key1, K2 key2, K3 key3, V v) {
        var map1 = map.computeIfAbsent(key1, k -> new HashMap<>());
        var map2 = map1.computeIfAbsent(key2, k -> new HashMap<>());
        map2.put(key3, v);
    }

    /**
     * Returns a list of all keys in the map
     *
     * @return ArrayList of values with type V
     */
    public List<V> getValues() {
        var l = new ArrayList<V>();
        for (Map<K2, Map<K3, V>> m1 : map.values()) {
            for (Map<K3, V> m2 : m1.values()) {
                l.addAll(m2.values());
            }
        }

        return l;
    }
}
