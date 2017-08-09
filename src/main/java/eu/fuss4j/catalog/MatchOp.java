package eu.fuss4j.catalog;

import java.util.function.BiFunction;

/**
 * Adds semantic / (fuzzy search related) contextual meaning to the otherwise quite generic {@link BiFunction}.
 *
 * @param <I> the type of the {@link Item item} to match
 * @param <P> the type of the pattern to match against
 * @param <R> the type of the match result
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 7, 2017
 */
@FunctionalInterface
public interface MatchOp<I extends Item, P, R extends Match<I>> extends BiFunction<I, P, R> {

    @Override
    default R apply(I item, P pattern) { return match(item, pattern); }

    R match(I item, P pattern);
}
