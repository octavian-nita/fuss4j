package eu.fuss4j;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Adds a bit of contextual meaning to the otherwise quite generic {@link BiFunction}.
 *
 * @param <ITEM>    the type of the item to match
 * @param <PATTERN> the type of the pattern to match against
 * @param <MATCH>   the type of the match result
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 7, 2017
 */
@FunctionalInterface
public interface MatchOp<ITEM, PATTERN, MATCH extends Match<ITEM>> extends BiFunction<ITEM, PATTERN, Optional<MATCH>> {

    @Override
    default Optional<MATCH> apply(ITEM item, PATTERN pattern) { return match(item, pattern); }

    Optional<MATCH> match(ITEM item, PATTERN pattern);
}
