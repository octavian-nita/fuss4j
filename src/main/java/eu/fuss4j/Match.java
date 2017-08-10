package eu.fuss4j;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public interface Match<ITEM> extends Comparable<Match<ITEM>> {

    ITEM getItem();

    int getScore();

    @Override
    default int compareTo(Match<ITEM> match) {
        return requireNonNull(match, "Cannot compare against null matches").getScore() - getScore();
    }
}
