package eu.fuss4j;

import java.util.Collection;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.Collections.unmodifiableSortedSet;
import static java.util.Optional.ofNullable;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public class StringMatch extends Match<String> {

    private final SortedSet<Location> locations;

    public StringMatch(String item, int score) { this(item, score, null); }

    public StringMatch(String item, int score, Collection<Location> locations) {
        super(item, score);

        if (locations == null) {
            this.locations = null;
        } else {
            final SortedSet<Location> locs = new TreeSet<>();
            for (Location loc : locations) {
                if (loc != null) {
                    locs.add(loc);
                }
            }
            this.locations = unmodifiableSortedSet(locs);
        }
    }

    public Optional<SortedSet<Location>> getLocations() { return ofNullable(locations); }
}
