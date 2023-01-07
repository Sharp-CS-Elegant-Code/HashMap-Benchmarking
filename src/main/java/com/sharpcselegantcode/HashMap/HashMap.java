package com.sharpcselegantcode.HashMap;

public interface HashMap<K, V> {

    V get(K key);

    V put(K key, V value);

    void delete(K key);

    /**
     * @see java.util.HashMap hash function
     */
    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
