package eu.fuss4j;

import eu.fuss4j.util.Highlight;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static org.junit.Assert.assertEquals;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 4, 2017
 */
public class LocationTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void endIndexLessThanStartIndexThrowsIndexOutOfBoundsException() { new Location(3, 2); }

    @Test(expected = IndexOutOfBoundsException.class)
    public void endIndexEqualToStartIndexThrowsIndexOutOfBoundsException() { new Location(3, 3); }

    @Test(expected = IndexOutOfBoundsException.class)
    public void negativeStartIndexThrowsIndexOutOfBoundsException() { new Location(-3, 2); }

    @Test
    public void endIndexMinusStartIndexYieldsLength() {
        final CharSequence seq = "abc";
        assertEquals("End index minus start index should yield sequence length", seq.length(),
                     new Location(0, seq.length()).length());
    }

    @Test
    public void usage() {

        final Collection<String> catalog = asList("Octavian", "Marian", "Aura", "Mirela");

        final Pattern pattern = compile("[Mr][aeiou]");

        final MatchOp<String, Pattern, MatchWithLocations<String>> fn = (i, p) -> {
            final Matcher matcher = p.matcher(i);
            final Collection<Location> locations = new ArrayList<>();

            int score = 0;
            while (matcher.find()) {
                score++;
                locations.add(new Location(matcher.start(), matcher.end()));
            }

            return score == 0
                   ? Optional.empty()
                   : Optional.of(new MatchWithLocations<>(new DefaultMatch<>(i, score), locations));
        };

        final Highlight highlight = new Highlight();

        // Take #1
        // catalog.stream().map(item -> fn.match(item, pattern)).filter(Optional::isPresent).map(Optional::get).map(
        //     m -> m.getLocations().isPresent() ? highlight.apply(m.getItem(), m.getLocations().get()) : m.getItem())
        //        .forEach(System.out::println);

        // Take #2
        // catalog.stream().map(item -> fn.match(item, pattern)).filter(Optional::isPresent).map(Optional::get)
        //        .map(m -> highlight.apply(m.getItem(), m.getLocations().orElse(null))).forEach(System.out::println);

        // Take #3
        // :) http://www.baeldung.com/java-filter-stream-of-optional
        catalog.stream().map(item -> fn.match(item, pattern)).flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
               .map(m -> highlight.apply(m.getItem(), m.getLocations().orElse(null))).forEach(System.out::println);

        // TODO modify the highlight to merge adjacent locations :) (where to place that functionality ;D )

        // ? Advantages:
        // ? - code is
        // ?   * concise / short
        // ?   * explicit
    }
}
