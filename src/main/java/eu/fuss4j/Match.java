package eu.fuss4j;

import static java.util.Objects.requireNonNull;

/**
 * Provides contextual information about a successful {@link MatchFn match} of an item against a pattern, including
 * the {@link #getItem() matched item} itself for convenience and a {@link #getScore() score} characterizing &quot;
 * how well&quot; the item matched the pattern.
 * <p/>
 * As several items might match a <em>given</em> pattern (a common requirement), matches are by default
 * {@link Comparable <q>naturally</q> ordered} by {@link #getScore() score} but note that this ordering
 * usually <strong>ONLY</strong> makes sense for matches against the <strong>SAME</strong> pattern!
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public interface Match<ITEM> extends Comparable<Match<ITEM>> {

    ITEM getItem();

    int getScore();

    @Override
    default int compareTo(Match<ITEM> match) {
        return requireNonNull(match, "Cannot compare a match against a null match").getScore() - getScore();
    }
}
