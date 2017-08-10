package eu.fuss4j;

import java.util.Collection;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.Collections.unmodifiableSortedSet;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 10, 2017
 */
public class MatchWithLocations<ITEM> implements Match<ITEM> {

    private final Match<ITEM> decorated;

    private final SortedSet<Location> locations;

    public MatchWithLocations(Match<ITEM> decorated, Collection<Location> locations) {
        this.decorated = requireNonNull(decorated, "Cannot attach locations to a null match");

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

    @Override
    public ITEM getItem() { return decorated.getItem(); }

    @Override
    public int getScore() { return decorated.getScore(); }

    public Optional<SortedSet<Location>> getLocations() { return ofNullable(locations); }
}
