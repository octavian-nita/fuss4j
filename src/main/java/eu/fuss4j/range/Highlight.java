package eu.fuss4j.range;

import java.util.Collection;
import java.util.function.BiFunction;

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
            this.suffix = this.prefix = "";
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

        int pos = 0;
        for (Range range : ranges) {

            if (pos < range.start) {
                buf.append(seq.subSequence(pos, range.start));
            }

            buf.append(prefix).append(seq.subSequence(range.start, range.end)).append(suffix);
            pos = range.end;

        }
        if (pos < seq.length()) {
            buf.append(seq.subSequence(pos, seq.length()));
        }

        return buf.toString();
    }
}
