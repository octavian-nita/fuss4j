package eu.fuss4j.cache;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 25, 2017
 * @implNote <code>null</code> arguments are not cached by default, the delegate function always being invoked in this
 * case
 */
public class MemoizedFn<T, R> implements Function<T, R> {

    protected final Function<T, R> delegate;

    protected final SoftCache<T, R> cache;

    public MemoizedFn(Function<T, R> delegate) { this(delegate, SoftCache.DEFAULT_MAX_SIZE); }

    public MemoizedFn(Function<T, R> delegate, int maxMemoized) {
        this.delegate = requireNonNull(delegate, "Cannot memoize a null function");
        this.cache = new SoftCache<>(maxMemoized);
    }

    @Override
    public R apply(T t) { return t == null ? delegate.apply(null) : cache.getOrCompute(t, delegate); }

    public static <T, R, F extends Function<T, R>> Function<T, R> memoize(F function) {
        return new MemoizedFn<>(function);
    }

    public static <T, R, F extends Function<T, R>> Function<T, R> memoizeAtMost(F function, int maxMemoized) {
        return new MemoizedFn<>(function, maxMemoized);
    }
}
