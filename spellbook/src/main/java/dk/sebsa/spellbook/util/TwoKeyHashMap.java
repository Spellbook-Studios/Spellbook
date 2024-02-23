package dk.sebsa.spellbook.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A hashmap with up to 2 key identifiers
 *
 * @param <K1> Type of first key
 * @param <K2> Type of second key
 * @param <V>  Type of value
 * @author sebs
 * @since 1.0.0
 */
public class TwoKeyHashMap<K1, K2, V> {
    private final Map<K1, Map<K2, V>> map; // The actual map. Holy jesus

    /**
     * A hashmap with up to 2 key identifiers
     */
    public TwoKeyHashMap() {
        map = new HashMap<>();
    }

    /**
     * Get a value from the map using the 4 keys
     *
     * @param key1 Key 1
     * @param key2 Key 2
     * @return The value identified by the 4 keys
     */
    public V get(K1 key1, K2 key2) {
        try {
            return map.get(key1).get(key2);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * If a value corresponding to the 2 keys exists within the map it is returned
     * Else V is put into the map and is returned
     *
     * @param key1 Key 1
     * @param key2 Key 2
     * @param v    Computed if value is absent
     * @return Either the value mapped to the keys or if non-existent the one supplied by v
     */
    public V getPut(K1 key1, K2 key2, Supplier<V> v) {
        V r = get(key1, key2);

        if (r == null) {
            r = v.get();
            put(key1, key2, r);
        }

        return r;
    }

    /**
     * Puts a value into the map identified by the four keys
     *
     * @param key1 Key 1
     * @param key2 Key 2
     * @param v    The value to store
     */
    public void put(K1 key1, K2 key2, V v) {
        var map1 = map.computeIfAbsent(key1, k -> new HashMap<>());
        map1.put(key2, v);
    }

    /**
     * Returns a list of all keys in the map
     *
     * @return ArrayList of values with type V
     */
    public List<V> getValues() {
        var l = new ArrayList<V>();
        for (Map<K2, V> m1 : map.values()) {
            l.addAll(m1.values());
        }

        return l;
    }
}
