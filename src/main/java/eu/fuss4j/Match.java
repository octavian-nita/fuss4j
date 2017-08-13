package eu.fuss4j;

import static java.util.Objects.requireNonNull;

/**
 * Provides contextual information about a successful {@link MatchFn match} of an item against a pattern, including
 * the {@link #getItem() matched item} itself for convenience and a {@link #getScore() score} characterizing &quot;
 * how well&quot; (or to what extent) the item matched the pattern. That said, the exact semantics (even relevance)
 * of the score depends mostly of the match algorithm employed.
 * <p/>
 * As several items might match a <em>given</em> pattern (a common requirement), matches are by default
 * {@link Comparable <q>naturally</q> ordered} by {@link #getScore() score} but note that this ordering
 * usually <strong>ONLY</strong> makes sense for matches against the <strong>SAME</strong> pattern!
 *
 * @param <ITEM> the type of the matched item
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public interface Match<ITEM> extends Comparable<Match<ITEM>> {

    ITEM getItem();

    default int getScore() { return 0; }

    @Override
    default int compareTo(Match<ITEM> match) {
        return requireNonNull(match, "Cannot compare a match against a null match").getScore() - getScore();
    }
}
