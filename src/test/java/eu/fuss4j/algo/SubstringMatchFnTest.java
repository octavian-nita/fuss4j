package eu.fuss4j.algo;

import eu.fuss4j.rang.MatchWithRanges;
import org.junit.*;

import java.util.Optional;

import static eu.fuss4j.algo.SubstringMatchFn.Occurrence.PREFIX;
import static eu.fuss4j.algo.SubstringMatchFn.Occurrence.SUFFIX;
import static org.junit.Assert.*;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 14, 2017
 */
@SuppressWarnings("Duplicates")
public class SubstringMatchFnTest {

    private SubstringMatchFn matchFn; // the system under test...

    @After
    public void tearDown() { matchFn = null; }

    @Before
    public void setUp() { matchFn = new SubstringMatchFn(); }

    @Test
    public void caseInsensitiveByDefault() {
        final String item = "prefix of string...";
        final String pref = "Prefix";

        matchFn.setOccurrence(PREFIX);
        Optional<MatchWithRanges<String>> match = matchFn.apply(item, pref);

        assertTrue(match.isPresent());
        assertEquals(item, match.get().getItem());
    }

    @Test
    public void caseInsensitivePrefixMatchMustBeExact() {
        final String item = " prefix of string...";
        final String pref = "Prefix";

        matchFn.setOccurrence(PREFIX);
        Optional<MatchWithRanges<String>> match = matchFn.apply(item, pref);

        assertFalse(match.isPresent());
    }

    @Test
    public void caseInsensitiveMatchesSuffix() {
        final String item = "string with suffix";
        final String suff = "Suffix";

        matchFn.setOccurrence(SUFFIX);
        Optional<MatchWithRanges<String>> match = matchFn.apply(item, suff);

        assertTrue(match.isPresent());
        assertEquals(item, match.get().getItem());
    }
}
