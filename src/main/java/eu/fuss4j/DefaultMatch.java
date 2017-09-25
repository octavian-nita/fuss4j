package eu.fuss4j;

import static java.util.Objects.requireNonNull;

/**
 * A straightforward, immutable {@link Match} implementation, appropriate for many typical use cases or to be extended.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 10, 2017
 */
public class DefaultMatch<ITEM> implements Match<ITEM> {

    private final ITEM item;

    private final int score;

    /**
     * @implSpec Equivalent to calling <code>new DefaultMatch(item, 0)</code>.
     */
    public DefaultMatch(ITEM item) { this(item, 0); }

    public DefaultMatch(ITEM item, int score) {
        this.item = requireNonNull(item, "Matched item cannot be null");
        this.score = score;
    }

    public ITEM getItem() { return item; }

    public int getScore() { return score; }

    @Override
    public String toString() { return item + ": " + score; }
}
