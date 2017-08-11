package eu.fuss4j.range;

import eu.fuss4j.DefaultMatch;
import eu.fuss4j.MatchFn;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static eu.fuss4j.range.MatchWithRanges.withRanges;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static org.junit.Assert.assertEquals;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 4, 2017
 */
public class RangeTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void endIndexLessThanStartIndexThrowsIndexOutOfBoundsException() { new Range(3, 2); }

    @Test(expected = IndexOutOfBoundsException.class)
    public void endIndexEqualToStartIndexThrowsIndexOutOfBoundsException() { new Range(3, 3); }

    @Test(expected = IndexOutOfBoundsException.class)
    public void negativeStartIndexThrowsIndexOutOfBoundsException() { new Range(-3, 2); }

    @Test
    public void endIndexMinusStartIndexYieldsLength() {
        final CharSequence seq = "abc";
        assertEquals("End index minus start index should yield sequence length", seq.length(),
                     new Range(0, seq.length()).length());
    }

    @Test
    public void usage() {

        final Collection<String> catalog = asList("Mirela", "Aura", "Marian", "Octavian");

        final Pattern pattern = compile("[Mr][aeiou]");

        final MatchFn<String, Pattern, MatchWithRanges<String>> fn = (i, p) -> {
            final Matcher m = p.matcher(i);

            int score = 0;
            final Collection<Range> ranges = new ArrayList<>();

            while (m.find()) {
                score++;
                ranges.add(new Range(m.start(), m.end()));
            }

            return score == 0 ? Optional.empty() : Optional.of(withRanges(new DefaultMatch<>(i, score), ranges));
        };

        final Highlight highlight = new Highlight();

        catalog.stream() //@fmt:off
               .map(item -> fn.match(item, pattern))
               .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
               .map(m -> highlight.apply(m.getItem(), m.getMergedRanges().orElse(null)))
               .forEach(System.out::println); //@fmt:on
    }
}
