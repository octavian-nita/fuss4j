package eu.fuss4j;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 10, 2017
 */
public class DefaultMatch<ITEM> implements Match<ITEM> {

    private final ITEM item;

    private final int score;

    public DefaultMatch(ITEM item, int score) {
        this.item = requireNonNull(item, "Matched item cannot be null");
        this.score = score;
    }

    public ITEM getItem() { return item; }

    public int getScore() { return score; }

    @Override
    public String toString() { return item + ": " + score; }
}
