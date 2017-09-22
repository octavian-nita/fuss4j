package eu.fuss4j.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * A poor man's bounded cache.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 16, 2017
 * @implSpec The default implementation is a {@link SoftReference}-based, LRU and concurrent.
 * @see <a href="https://www.ibm.com/developerworks/library/j-jtp01246/index.html">Plugging memory leaks with soft
 * references</a>
 */
public class SoftCache<K, V> {

    protected transient SoftReference<Map<K, V>> cacheRef;

    protected final int maxSize;

    protected final Lock lock;

    /**
     * @implSpec Equivalent to calling <code>new SoftCache(4096)</code>.
     */
    public SoftCache() { this(4096); }

    public SoftCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Maximum cache size must be greater than 0");
        }
        this.maxSize = maxSize;
        this.lock = new ReentrantLock();
        this.cacheRef = new SoftReference<>(createCache(this.maxSize));
    }

    public V getOrCompute(K key, Function<? super K, ? extends V> mappingFn) {
        lock.lock();
        try {
            return cache().computeIfAbsent(key, mappingFn);
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        lock.lock();
        try {
            cacheRef = null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return the backing {@link Map structure} that holds actual data
     * @implNote The default method implementation is not thread-safe / synchronized.
     */
    protected Map<K, V> cache() {
        Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        if (cache == null) {
            cacheRef = new SoftReference<>(cache = createCache(maxSize));
        }
        return cache;
    }

    /**
     * @param maxSize the maximum number of elements the cache is able to store
     * @return a new instance of the backing {@link Map structure} to hold actual data
     */
    protected Map<K, V> createCache(int maxSize) {
        return new LinkedHashMap<K, V>(16, 0.75f, true /* LRU... */) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > maxSize; }
        };
    }
}
