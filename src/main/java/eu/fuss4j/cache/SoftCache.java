package eu.fuss4j.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * A poor man's {@link SoftReference}-based, LRU and concurrent by default, cache.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 16, 2017
 * @see <a href="https://www.ibm.com/developerworks/library/j-jtp01246/index.html">Plugging memory leaks with soft
 * references</a>
 */
public class SoftCache<K, V> {

    protected transient SoftReference<Map<K, V>> cacheRef;

    protected final ReadWriteLock rwLock;

    protected final int maxSize;

    public SoftCache() { this(16384); }

    public SoftCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Maximum cache size must be greater than 0");
        }
        this.maxSize = maxSize;
        this.rwLock = new ReentrantReadWriteLock();
        this.cacheRef = new SoftReference<>(createCache(this.maxSize));
    }

    public V getOrCompute(K key, Function<? super K, ? extends V> mappingFn) {
        rwLock.writeLock().lock();
        try {
            return cache().computeIfAbsent(key, mappingFn);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void clear() {
        rwLock.writeLock().lock();
        try {
            cacheRef = null;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    protected Map<K, V> cache() {
        Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        if (cache == null) {
            cacheRef = new SoftReference<>(cache = createCache(maxSize));
        }
        return cache;
    }

    protected Map<K, V> createCache(int maxSize) {
        return new LinkedHashMap<K, V>(maxSize, 0.75f, true /* LRU... */) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > maxSize; }
        };
    }
}
