package eu.fuss4j.misc;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.text.Normalizer.Form.NFD;
import static java.text.Normalizer.normalize;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 14, 2017
 */
public class Normalize implements Function<CharSequence, String> {

    public static String norm(CharSequence seq) { return NORM.apply(seq); }

    @Override
    public String apply(CharSequence seq) {
        Map<String, String> cache = normalizedCache == null ? null : normalizedCache.get();
        if (cache == null) {
            normalizedCache = new SoftReference<>(cache = new HashMap<>(8192));
        }

        final String key = seq.toString();

        String normalized = cache.get(key);
        if (normalized == null) {
            cache.put(key, normalized = ASCII.matcher(normalize(seq, NFD)).replaceAll(""));
        }
        return normalized;
    }

    /**
     * A poor man's cache for already normalized char sequences...
     *
     * @see <a href="https://www.ibm.com/developerworks/library/j-jtp01246/index.html">Plugging memory leaks with soft
     * references</a>
     */
    protected SoftReference<Map<String, String>> normalizedCache = new SoftReference<>(new HashMap<>(8192));

    protected static final Pattern ASCII = Pattern.compile("[^\\p{ASCII}]");

    protected static final Normalize NORM = new Normalize();
}
