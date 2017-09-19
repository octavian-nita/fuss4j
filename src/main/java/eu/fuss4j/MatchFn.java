package eu.fuss4j;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Specifies a function / algorithm that tries to {@link #match(Object, Object) match} an item against a pattern and,
 * upon success, returns {@link Match contextual information about the match}.
 * <p>
 * Adds {@link #match(Object, Object) a bit of semantics} to the otherwise quite generic {@link BiFunction}.
 *
 * @param <ITEM>    the type of the item to match; usually <em>has</em> or <em>is</em> a character sequence
 *                  representation
 * @param <PATTERN> the type of the pattern to match against; usually <em>has</em> or <em>is</em> a character
 *                  sequence representation
 * @param <MATCH>   the type of the result (contextual information) in case of a successful match
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 7, 2017
 */
@FunctionalInterface
public interface MatchFn<ITEM, PATTERN, MATCH extends Match<ITEM>> extends BiFunction<ITEM, PATTERN, Optional<MATCH>> {

    @Override
    default Optional<MATCH> apply(ITEM item, PATTERN pattern) { return match(item, pattern); }

    Optional<MATCH> match(ITEM item, PATTERN pattern);
}
