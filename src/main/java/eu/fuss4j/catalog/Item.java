package eu.fuss4j.catalog;

/**
 * Item that can be searched / matched against a pattern based on its {@link #toCharSequence() representation as a
 * sequence of char values}.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 7, 2017
 */
public interface Item {

    /**
     * @return {@code this} item's representation as a sequence of characters, that can be the input of a {@link MatchOp
     * search algorithm}
     */
    CharSequence toCharSequence();
}
