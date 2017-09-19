package eu.fuss4j.range;

import eu.fuss4j.matches.MatchWithRanges;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Highlights, by enclosing between a prefix and a suffix, {@link Range ranges} in a character sequence.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public final class Highlight implements BiFunction<CharSequence, Collection<Range>, String> {

    private final String prefix;

    private final String suffix;

    public Highlight(String prefix, String suffix) {
        this.prefix = prefix == null ? "" : prefix;
        this.suffix = suffix == null ? "" : suffix;
    }

    public Highlight(char prefix, char suffix) {
        this.prefix = Character.toString(prefix);
        this.suffix = Character.toString(suffix);
    }

    public Highlight(String tag) {
        if (tag == null) {
            this.prefix = this.suffix = "";
        } else {
            this.prefix = "<" + tag + ">";
            this.suffix = "</" + tag + ">";
        }
    }

    public Highlight() { this('[', ']'); }

    @Override
    public String apply(CharSequence seq, Collection<Range> ranges) {
        if (seq == null) {
            return "";
        }

        if (ranges == null || ranges.isEmpty()) {
            return seq.toString();
        }

        final StringBuilder buf = new StringBuilder();

        int idx = 0;
        for (Range range : ranges) {

            if (idx < range.start) {
                buf.append(seq.subSequence(idx, range.start));
            }

            buf.append(prefix).append(seq.subSequence(range.start, range.end)).append(suffix);
            idx = range.end;

        }
        if (idx < seq.length()) {
            buf.append(seq.subSequence(idx, seq.length()));
        }

        return buf.toString();
    }

    public <ITEM> String on(MatchWithRanges<ITEM> match,
                            Function<MatchWithRanges<ITEM>, Optional<? extends Collection<Range>>> rangesSupplier) {
        return match == null
               ? ""
               : apply(match.getItem() == null ? null : match.getItem().toString(),
                       (rangesSupplier == null ? match.getRanges() : rangesSupplier.apply(match)).orElse(null));
    }

    public <ITEM> String on(MatchWithRanges<ITEM> match) { return on(match, null); }
}
