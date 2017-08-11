package eu.fuss4j.range;

import eu.fuss4j.Match;

import java.util.Collection;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.Collections.unmodifiableSortedSet;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.*;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 10, 2017
 */
public class MatchWithRanges<ITEM> implements Match<ITEM> {

    private final Match<ITEM> decorated;

    private final SortedSet<Range> ranges;

    public MatchWithRanges(Match<ITEM> match, Collection<Range> ranges) {
        this.decorated = requireNonNull(match, "Cannot attach ranges to a null match");

        if (ranges == null) {
            this.ranges = null;
        } else {
            final SortedSet<Range> locs = new TreeSet<>();
            for (Range loc : ranges) {
                if (loc != null) {
                    locs.add(loc);
                }
            }
            this.ranges = unmodifiableSortedSet(locs);
        }
    }

    public static <ITEM> MatchWithRanges<ITEM> withRanges(Match<ITEM> match, Collection<Range> ranges) {
        return new MatchWithRanges<>(match, ranges);
    }

    @Override
    public ITEM getItem() { return decorated.getItem(); }

    @Override
    public int getScore() { return decorated.getScore(); }

    public Optional<SortedSet<Range>> getRanges() { return ofNullable(ranges); }

    public Optional<SortedSet<Range>> getMergedRanges() {
        if (ranges == null) {
            return empty();
        }

        final TreeSet<Range> merged = new TreeSet<>();

        Range prev = null;
        for (Range curr : ranges) {
            if (prev == null) {
                merged.add(prev = curr);
                continue;
            }

            if (prev.end == curr.start) {
                merged.remove(prev);
                merged.add(prev = new Range(prev.start, curr.end));
            }
        }

        return of(unmodifiableSortedSet(merged));
    }
}
