package eu.fussy4j;

import java.util.*;

import static java.lang.Integer.compare;
import static java.util.Collections.unmodifiableSortedSet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 7, 2017
 */
public class Match<I extends Item> {

    private final I item;

    private final int score;

    private final SortedSet<Location> locations;

    public Match(I item) { this(item, Integer.MIN_VALUE, null); }

    public Match(I item, int score, Collection<Location> locations) {
        if (item == null) {
            throw new NullPointerException("Matched item cannot be null");
        }

        this.item = item;
        this.score = score;
        this.locations = locations == null
                         ? null
                         : unmodifiableSortedSet(
                             locations.stream().filter(Objects::nonNull).collect(toCollection(TreeSet::new)));
    }

    public I getItem() { return item; }

    public int getScore() { return score; }

    public Optional<SortedSet<Location>> getLocations() { return ofNullable(locations); }

    public String highlight() {
        final CharSequence seq = item.getCharSequence();

        if (locations == null || locations.isEmpty()) {
            return seq.toString();
        }

        final StringBuilder buf = new StringBuilder();

        int idx = 0;
        for (Location loc : locations) {

            if (idx < loc.start) {
                buf.append(seq.subSequence(idx, loc.start));
            }

            buf.append(highlightSubSequence(seq.subSequence(loc.start, loc.end)));
            idx = loc.end;

        }
        if (idx < seq.length()) {
            buf.append(seq.subSequence(idx, seq.length()));
        }

        return buf.toString();
    }

    protected String highlightSubSequence(CharSequence seq) { return "[" + seq + "]"; }

    public static final Comparator<Match> CMP_SCORE = (m1, m2) -> {
        if (m1 == null || m2 == null) {
            throw new NullPointerException("Cannot compare null matches");
        }
        return m2.getScore() - m1.getScore();
    };

    public static final Comparator<Match> CMP_ALPHA = (m1, m2) -> {
        if (m1 == null || m2 == null) {
            throw new NullPointerException("Cannot compare null matches");
        }
        return m1.getItem().getCharSequence().toString().compareTo(m2.getItem().getCharSequence().toString());
    };

    public static class Location implements Comparable<Location> {

        public final int start;

        public final int end;

        /**
         * @param start the start index within the {@link #item matched item's} {@link Item#getCharSequence() char
         *              sequence representation} where ({@code end - start}) characters in a pattern are found
         *              sequentially; inclusive
         * @param end   the end index within the {@link #item matched item's} {@link Item#getCharSequence() char
         *              sequence representation} where characters in a pattern do not match anymore; exclusive
         * @throws IndexOutOfBoundsException if {@code start} is negative or if {@code start} is greater than or equal
         *                                   to {@code end}
         */
        public Location(int start, int end) {
            if (start < 0) {
                throw new IndexOutOfBoundsException("Char sequence start index out of range: " + start);
            }
            if (start >= end) {
                throw new IndexOutOfBoundsException(
                    "Char sequence end index lower than or equal to start index: " + end);
            }
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Location loc) {
            final int cmpStart = compare(start, loc.start);
            return cmpStart == 0 ? compare(end, loc.end) : cmpStart;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o != null && getClass() == o.getClass() && start == ((Location) o).start &&
                                end == ((Location) o).end;
        }

        @Override
        public int hashCode() { return (17 * 37 + start) * 37 + end; }

        @Override
        public String toString() { return "loc[" + start + "..." + end + "]"; }
    }
}
