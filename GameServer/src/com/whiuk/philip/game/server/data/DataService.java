package com.whiuk.philip.game.server.data;

/**
 * @author Philip
 * @param <K> Key
 * @param <V> Value
 *
 */
public interface DataService<K,V> {

    /**
     * @param key
     * @return value
     */
    V get(K key);

}
