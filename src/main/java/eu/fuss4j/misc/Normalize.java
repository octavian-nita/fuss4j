package eu.fuss4j.misc;

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
        return cache == null ? ASCII.matcher(normalize(seq, NFD)).replaceAll("") : cache
            .getOrCompute(seq.toString(), (k) -> ASCII.matcher(normalize(seq, NFD)).replaceAll(""));
    }

    public static String norm(CharSequence seq) { return NORM.apply(seq); }

    protected SoftCache<String, String> cache = new SoftCache<>();

    protected static final Pattern ASCII = Pattern.compile("[^\\p{ASCII}]");

    protected static final Normalize NORM = new Normalize();
}
