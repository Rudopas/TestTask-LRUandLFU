package com.company.cache;

import com.company.cache.storages.LFU;
import com.company.cache.storages.LRU;

public class Cache<K, V> {
    public static String MAX_SIZE_ERROR = "maxSize should be more than 0";
    public static String ALGORITHM_ERROR = "caching algorithm was not found";
    public enum Algorithm {LRU, LFU}

    private Storage<K, V> storage;

    public Cache(Algorithm algorithm, int maxSize){
        if(maxSize <= 0) {
            throw new IllegalArgumentException(MAX_SIZE_ERROR);
        }
        switch (algorithm) {
            case LFU:
                storage = new LFU<>(maxSize);
                break;
            case LRU:
                storage = new LRU<>(maxSize);
                break;
            default: // It's impossible )
                throw new IllegalArgumentException(ALGORITHM_ERROR);
        }
    }

    V get(K key) {
        return storage.get(key);
    }

    V put(K key, V value) {
        return storage.put(key, value);
    }

    int size() {
        return storage.size();
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}
