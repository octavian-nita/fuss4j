package eu.fuss4j;

import eu.fuss4j.util.Highlight;
import org.junit.Test;

import java.util.Arrays;

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


        System.out
            .println(new Highlight("em").apply("Marian Venin", Arrays.asList(new Location(0, 1), new Location(7, 8))));
    }
}
