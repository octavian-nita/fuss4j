package eu.fuss4j.range;

import java.util.*;
import java.util.function.Function;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 27, 2017
 */
public final class MergeRanges implements Function<Collection<Range>, SortedSet<Range>> {

    @Override
    public SortedSet<Range> apply(Collection<Range> ranges) {

        if (ranges == null) {
            return null;
        }

        final SortedSet<Range> sortedRanges =
            ranges instanceof SortedSet ? (SortedSet<Range>) ranges : new TreeSet<>(ranges);

        final SortedSet<Range> mergedRanges = new TreeSet<>();

        Range prev = null;
        for (Range curr : sortedRanges) {

            if (prev != null && prev.end == curr.start) {
                mergedRanges.remove(prev);
                prev = new Range(prev.start, curr.end);
            } else {
                prev = curr;
            }

            mergedRanges.add(prev);
        }

        return mergedRanges;
    }

    public static SortedSet<Range> merge(Collection<Range> ranges) { return MERGE.apply(ranges); }

    private static final MergeRanges MERGE = new MergeRanges();
}
