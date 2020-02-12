package com.company.cache.storages;

import com.company.cache.Storage;
import java.util.*;

public class LFU<K, V> implements Storage<K, V> {

    private final int MAX_SIZE;
    private HashMap<K, V> storage;
    private HashMap<K, Long> keyFrequency;
    private TreeMap<Long, HashSet<K>> sortedFrequencies;

    public LFU(int maxSize) {
        MAX_SIZE = maxSize;
        storage = new HashMap<>();
        keyFrequency = new HashMap<>();
        sortedFrequencies = new TreeMap<>();
    }

    private void eviction() {
        Long minFrequency = sortedFrequencies.firstKey();
        K evictionKey = sortedFrequencies.get(minFrequency).iterator().next();
        delFromSortedFrequencies(evictionKey, minFrequency);
        keyFrequency.remove(evictionKey);
        storage.remove(evictionKey);
    }

    private void frequencyIncrement(K key) {
        Long frequency = keyFrequency.compute(key, (k, f) -> f + 1L);
        delFromSortedFrequencies(key, frequency - 1);
        sortedFrequencies.computeIfAbsent(frequency, keys -> new HashSet<>()).add(key);
    }

    private void delFromSortedFrequencies(K key, Long frequency) {
        if (sortedFrequencies.get(frequency).size() > 1) {
            sortedFrequencies.get(frequency).remove(key);
        } else {
            sortedFrequencies.remove(frequency);
        }
    }

    @Override
    public V put(K key, V value) {
        if (storage.get(key) != null){
            storage.put(key, value);
            frequencyIncrement(key); //maybe it is bad idea, because we use get(key,value) in if condition
        }
        if (storage.size() == MAX_SIZE) {
            eviction();
        }
        V oldValue = storage.put(key, value);
        Long frequency = keyFrequency.computeIfAbsent(key, f -> 1L);
        sortedFrequencies.computeIfAbsent(frequency, keys -> new HashSet<>()).add(key);
        return oldValue;
    }

    @Override
    public V get(K key) {
        V value = storage.get(key);
        if (value != null) {
            frequencyIncrement(key);
        }
        return value;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}
