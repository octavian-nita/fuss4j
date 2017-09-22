package eu.fuss4j.cache;

import java.util.function.Function;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 22, 2017
 */
public class MemoizedCharSequenceFn<R> implements Function<CharSequence, R> {

    private SoftCache<CharSequence, R> cache = new SoftCache<>();

    @Override
    public R apply(CharSequence sequence) {
        return null;
    }
}
