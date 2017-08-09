package eu.fuss4j;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public class Match<ITEM> implements Comparable<Match<ITEM>> {

    private final ITEM item;

    private final int score;

    public Match(ITEM item, int score) {
        this.item = requireNonNull(item, "Matched item cannot be null");
        this.score = score;
    }

    public ITEM getItem() { return item; }

    public int getScore() { return score; }

    @Override
    public int compareTo(Match<ITEM> match) {
        return requireNonNull(match, "Cannot compare against null matches").getScore() - getScore();
    }

    @Override
    public String toString() { return item + ": " + score; }
}
