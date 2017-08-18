package eu.fuss4j.glob;

import eu.fuss4j.misc.SoftCache;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 18, 2017
 */
public class GlobToPattern extends GlobSupport implements Function<String, Pattern> {

    public GlobToPattern() {}

    public GlobToPattern(GlobFlavor flavor) { super(flavor); }

    @Override
    public Pattern apply(String glob) {
        return patternCache.getOrCompute(new PatternCacheKey(glob, flavor), (key) -> {
            String regex;

            switch (key.flavor) {
            case ANY:
                final String dosRE = toRegex(key.glob, true);
                final String unxRE = toRegex(key.glob, false);
                regex = unxRE.equals(dosRE) ? unxRE : "(?:" + unxRE + ")|(?:" + dosRE + ")";
                break;
            case DOS:
                regex = toRegex(key.glob, true);
                break;
            case UNIX:
                regex = toRegex(key.glob, false);
                break;
            default:
                throw new RuntimeException("Unsupported glob flavor: " + flavor);
            }

            return Pattern.compile(regex);
        });
    }

    private static final SoftCache<PatternCacheKey, Pattern> patternCache = new SoftCache<>();

    private static final class PatternCacheKey {

        private final String glob;

        private final GlobFlavor flavor;

        private PatternCacheKey(String glob, GlobFlavor flavor) {
            this.glob = Objects.requireNonNull(glob);
            this.flavor = flavor == null ? GlobFlavor.ANY : flavor;
        }

        @Override
        public int hashCode() { return Objects.hash(glob, flavor); }

        @Override
        public boolean equals(Object o) {
            return this == o ||
                   o != null && getClass() == o.getClass() && Objects.equals(glob, ((PatternCacheKey) o).glob) &&
                   Objects.equals(flavor, ((PatternCacheKey) o).flavor);
        }
    }
}
