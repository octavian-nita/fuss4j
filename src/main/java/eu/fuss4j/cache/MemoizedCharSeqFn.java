package eu.fuss4j.cache;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 25, 2017
 */
public class MemoizedCharSeqFn<R> implements Function<CharSequence, R> {

    protected final Function<CharSequence, R> delegate;

    protected final SoftCache<CharSequence, R> cache;

    public MemoizedCharSeqFn(Function<CharSequence, R> delegate) { this(delegate, SoftCache.DEFAULT_MAX_SIZE); }

    public MemoizedCharSeqFn(Function<CharSequence, R> delegate, int maxMemoized) {
        this.delegate = requireNonNull(delegate, "Cannot memoize a null function");
        this.cache = new SoftCache<>(maxMemoized);
    }

    @Override
    public R apply(CharSequence sequence) {
        return null;
    }
}
