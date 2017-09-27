package eu.fuss4j.cache;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 25, 2017
 * @implNote <code>null</code> arguments are not cached, the delegate function is always invoked in this case
 */
public final class MemoizedFn<T, R> implements Function<T, R> {

    private final Function<T, R> delegate;

    private final SoftCache<T, R> cache;

    public MemoizedFn(Function<T, R> delegate) { this(delegate, SoftCache.DEFAULT_MAX_SIZE); }

    public MemoizedFn(Function<T, R> delegate, int maxMemoized) {
        this.delegate = Objects.requireNonNull(delegate, "Cannot memoize a null function");
        this.cache = new SoftCache<>(maxMemoized);
    }

    @Override
    public R apply(T t) { return t == null ? delegate.apply(null) : cache.getOrCompute(t, delegate); }

    public static <T, R> MemoizedFn<T, R> memoize(Function<T, R> fn) { return new MemoizedFn<>(fn); }

    public static <T, R> MemoizedFn<T, R> memoizeAtMost(Function<T, R> fn, int maxMemoized) {
        return new MemoizedFn<>(fn, maxMemoized);
    }
}
