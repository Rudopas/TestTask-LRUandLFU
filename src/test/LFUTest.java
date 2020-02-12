package test;

import com.company.cache.Storage;
import com.company.cache.storages.LFU;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LFUTest {
    private int maxSize;
    private Storage<Integer, Integer> storage;

    @BeforeEach
    void init() {
        maxSize = 4;
        storage = new LFU(maxSize);
        for(int i = 0; i < maxSize; i++) {
            storage.put(i, i);
        }
    }

    @Test
    void put() {
        Integer key = 1;
        Integer value = 111;
        Integer storageSize = storage.size();
        Integer oldValue = storage.put(key, value);
        assertEquals(value, storage.get(key));
        assertEquals(oldValue, Integer.valueOf(1));
        assertEquals(storageSize, storage.size());
    }
    
    @Test
    void putNewKey() {
        Integer key = 5;
        Integer value = 5;
        assertEquals(value, storage.get(key));
    }

    @Test
    void get() {
        Integer key = 1;
        assertEquals(key, storage.get(key));
    }

    @Test
    void size() {
        assertEquals(maxSize, storage.size());
        storage.put(maxSize + 1, maxSize + 1);
        assertEquals(maxSize, storage.size());
    }

    @Test
    void lfu() {
        Integer key = maxSize / 2;
        for (int i = 0; i < maxSize; i++) {
            if(i != key) {
                storage.get(i);
                storage.get(i);
            }
        }
        storage.get(key);
        storage.put(maxSize + 1, maxSize + 1);
        assertNull(storage.get(key));
        storage.put(maxSize + 2, maxSize + 2);
        assertNull(storage.get(maxSize + 1));
    }
}
