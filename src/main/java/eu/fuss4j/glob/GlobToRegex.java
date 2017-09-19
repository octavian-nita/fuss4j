package eu.fuss4j.glob;

import eu.fuss4j.cache.SoftCache;

import java.util.function.UnaryOperator;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 18, 2017
 */
public class GlobToRegex extends GlobSupport implements UnaryOperator<String> {

    GlobToRegex() {}

    GlobToRegex(GlobFlavor flavor) { super(flavor); }

    @Override
    public String apply(String glob) {
        switch (flavor) {
        case ANY:
            final String dosRE = dosRegexCache.getOrCompute(glob, (g) -> toRegex(glob, true));
            final String unxRE = unixRegexCache.getOrCompute(glob, (g) -> toRegex(glob, false));
            return unxRE.equals(dosRE) ? unxRE : ("(?:" + unxRE + ")|(?:" + dosRE + ")");
        case DOS:
            return dosRegexCache.getOrCompute(glob, (g) -> toRegex(glob, true));
        case UNIX:
            return unixRegexCache.getOrCompute(glob, (g) -> toRegex(glob, false));
        default:
            throw new RuntimeException("Unsupported glob flavor: " + flavor);
        }
    }

    private static final SoftCache<String, String> unixRegexCache = new SoftCache<>();

    private static final SoftCache<String, String> dosRegexCache = new SoftCache<>();
}
