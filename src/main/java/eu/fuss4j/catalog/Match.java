package eu.fuss4j.catalog;

import eu.fuss4j.Location;

import java.util.*;

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
        this.locations = locations == null ? null : unmodifiableSortedSet(
            locations.stream().filter(Objects::nonNull).collect(toCollection(TreeSet::new)));
    }

    public I getItem() { return item; }

    public int getScore() { return score; }

    public Optional<SortedSet<Location>> getLocations() { return ofNullable(locations); }

    public String highlight() {
        final CharSequence seq = item.toCharSequence();

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
        return m1.getItem().toCharSequence().toString().compareTo(m2.getItem().toCharSequence().toString());
    };

}
