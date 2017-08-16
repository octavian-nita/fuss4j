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

    @Override
    public String apply(CharSequence seq) {
        return cache().computeIfAbsent(seq.toString(), (key) -> ASCII.matcher(normalize(seq, NFD)).replaceAll(""));
    }

    public static String norm(CharSequence seq) { return NORM.apply(seq); }

    /**
     * A poor man's cache for already normalized character sequences... Access it by calling {@link #cache()}.
     *
     * @see <a href="https://www.ibm.com/developerworks/library/j-jtp01246/index.html">Plugging memory leaks with soft
     * references</a>
     */
    protected SoftReference<Map<String, String>> normalizedCache = new SoftReference<>(new HashMap<>(8192));

    protected Map<String, String> cache() {
        Map<String, String> cache = normalizedCache == null ? null : normalizedCache.get();
        if (cache == null) {
            normalizedCache = new SoftReference<>(cache = new HashMap<>(8192));
        }
        return cache;
    }

    protected static final Pattern ASCII = Pattern.compile("[^\\p{ASCII}]");

    protected static final Normalize NORM = new Normalize();
}
