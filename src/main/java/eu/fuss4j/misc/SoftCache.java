package eu.fuss4j.misc;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * A poor man's cache... Access it by calling {@link #getOrCompute(Object, Function)}.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 16, 2017
 * @see <a href="https://www.ibm.com/developerworks/library/j-jtp01246/index.html">Plugging memory leaks with soft
 * references</a>
 */
public final class SoftCache<K, V> {

    private final int maxCapacity;

    private transient SoftReference<ConcurrentMap<K, V>> cacheRef;

    private ConcurrentMap<K, V> cache() {
        ConcurrentMap<K, V> cache = cacheRef == null ? null : cacheRef.get();
        if (cache == null) {
            cacheRef = new SoftReference<>(cache = new ConcurrentHashMap<>(this.maxCapacity));
        }
        return cache;
    }

    public SoftCache() { this(4096); }

    public SoftCache(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Maximum cache capacity must be greater than 0");
        }
        this.maxCapacity = maxCapacity;
        this.cacheRef = new SoftReference<>(new ConcurrentHashMap<>(this.maxCapacity));
    }

    public V getOrCompute(K key, Function<? super K, ? extends V> mappingFn) {
        return cache().computeIfAbsent(key, mappingFn);
    }
}
