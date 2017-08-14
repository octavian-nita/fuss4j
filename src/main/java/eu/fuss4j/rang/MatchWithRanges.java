package eu.fuss4j.rang;

import eu.fuss4j.DefaultMatch;
import eu.fuss4j.Match;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSortedSet;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.*;

/**
 * <a href="https://sourcemaking.com/design_patterns/decorator">Decorates</a> a {@link Match} with a (still {@link
 * Optional optional}!) {@link #getRanges() sorted set of ranges} where the item matched the pattern.
 * <p/>
 * Kind of implies that the item and the pattern both have or are character sequence representations...
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 10, 2017
 */
public class MatchWithRanges<ITEM> implements Match<ITEM> {

    public static <ITEM> MatchWithRanges<ITEM> withRanges(Match<ITEM> match, Range... ranges) {
        return withRanges(match, ranges == null ? null : asList(ranges));
    }

    public static <ITEM> MatchWithRanges<ITEM> withRanges(Match<ITEM> match, Collection<Range> ranges) {
        return new MatchWithRanges<>(match, ranges);
    }

    public static <ITEM> MatchWithRanges<ITEM> withRanges(ITEM item, Range... ranges) {
        return withRanges(item, ranges == null ? null : asList(ranges));
    }

    public static <ITEM> MatchWithRanges<ITEM> withRanges(ITEM item, Collection<Range> ranges) {
        return new MatchWithRanges<>(new DefaultMatch<>(item), ranges);
    }

    public static <ITEM> MatchWithRanges<ITEM> withRanges(ITEM item, int score, Range... ranges) {
        return withRanges(item, score, ranges == null ? null : asList(ranges));
    }

    public static <ITEM> MatchWithRanges<ITEM> withRanges(ITEM item, int score, Collection<Range> ranges) {
        return new MatchWithRanges<>(new DefaultMatch<>(item, score), ranges);
    }

    private final Match<ITEM> match;

    private final SortedSet<Range> ranges;

    public MatchWithRanges(Match<ITEM> match, Collection<Range> ranges) {
        this.match = requireNonNull(match, "Cannot associate ranges with a null match");

        if (ranges == null) {
            this.ranges = null;
        } else {
            final SortedSet<Range> rangs = new TreeSet<>();
            for (Range range : ranges) {
                if (range != null) {
                    rangs.add(range);
                }
            }
            this.ranges = unmodifiableSortedSet(rangs);
        }
    }

    @Override
    public ITEM getItem() { return match.getItem(); }

    @Override
    public int getScore() { return match.getScore(); }

    public Optional<SortedSet<Range>> getRanges() { return ofNullable(ranges); }

    public Optional<SortedSet<Range>> getMergedRanges() {
        if (ranges == null) {
            return empty();
        }

        final TreeSet<Range> merged = new TreeSet<>();

        Range prev = null;
        for (Range curr : ranges) {

            if (prev != null && prev.end == curr.start) {
                merged.remove(prev);
                prev = new Range(prev.start, curr.end);
            } else {
                prev = curr;
            }
            merged.add(prev);

        }

        return of(unmodifiableSortedSet(merged));
    }
}
