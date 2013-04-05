package com.whiuk.philip.game.server.data;

/**
 * @author Philip
 * @param <K> Key
 * @param <V> Value
 *
 */
public interface DataService<K, V> {

    /**
     * @param key Key
     * @return value
     */
    V get(K key);

    /**
     * @param key Key
     * @param value Value
     */
    void put(K key, V value);

}
