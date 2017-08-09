package eu.fuss4j;

import java.util.*;

import static java.util.Collections.unmodifiableSortedSet;
import static java.util.Objects.requireNonNull;
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

    public String highlight() {
        String item = getItem();

        if (locations == null || locations.isEmpty()) {
            return item;
        }

        final StringBuilder buf = new StringBuilder();

        int idx = 0;
        for (Location loc : locations) {

            if (idx < loc.start) {
                buf.append(item.subSequence(idx, loc.start));
            }

            buf.append(highlightSubSequence(item.subSequence(loc.start, loc.end)));
            idx = loc.end;

        }
        if (idx < item.length()) {
            buf.append(item.subSequence(idx, item.length()));
        }

        return buf.toString();
    }

    protected String highlightSubSequence(CharSequence seq) { return "[" + seq + "]"; }

    public static final Comparator<StringMatch> CMP_ALPHA = (m1, m2) -> {
        requireNonNull(m1, "Cannot compare null matches");
        requireNonNull(m2, "Cannot compare null matches");
        return m1.getItem().compareTo(m2.getItem());
    };
}
