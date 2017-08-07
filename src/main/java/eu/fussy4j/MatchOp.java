package eu.fussy4j;

import java.util.function.BiFunction;

/**
 * Adds semantic / (fuzzy search related) contextual meaning to the otherwise quite generic {@link BiFunction}.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 7, 2017
 */
@FunctionalInterface
public interface MatchOp<P, I, R> extends BiFunction<P, I, R> {

    @Override
    default R apply(P pattern, I item) { return match(pattern, item); }

    R match(P pattern, I item);
}
