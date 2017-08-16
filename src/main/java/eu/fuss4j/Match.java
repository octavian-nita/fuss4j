package eu.fuss4j;

import static java.util.Objects.requireNonNull;

/**
 * Provides contextual information about a successful {@link MatchFn#match(Object, Object) match} of an item against a
 * pattern, including the {@link #getItem() matched item} itself for convenience and a {@link #getScore() score}
 * characterizing &quot;how well&quot; / to what extent the item matched the pattern (especially useful in fuzzy
 * matching). The score's exact semantics (or even relevance) depends mostly on the employed matching algorithm.
 * <p/>
 * As several items may match one <em>given</em> pattern (a common requirement), matches are, by default, {@link
 * Comparable <q>naturally</q> ordered} by {@link #getScore() score} but note that this ordering usually
 * <strong>ONLY</strong> makes sense when matching against the <strong>SAME</strong> pattern!
 *
 * @param <ITEM> the type of the matched item; usually has or is a character sequence representation
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
