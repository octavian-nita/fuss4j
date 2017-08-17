package eu.fuss4j.misc;

import java.util.function.Function;
import java.util.regex.Pattern;

import static java.text.Normalizer.Form.NFD;
import static java.text.Normalizer.normalize;

/**
 * &quot;Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ&quot; =&gt; &quot;This is a funky String&quot;
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 14, 2017
 * @see <a href="https://blog.mafr.de/2015/10/10/normalizing-text-in-java/">Normalizing Text in Java</a>
 */
public class Normalize implements Function<CharSequence, String> {

    @Override
    public String apply(CharSequence seq) {
        // By default, if the already-normalized cache is null, we do not use a cache at all
        return normalizedCache == null ? ASCII.matcher(normalize(seq, NFD)).replaceAll("") : normalizedCache
            .getOrCompute(seq.toString(), (k) -> ASCII.matcher(normalize(seq, NFD)).replaceAll(""));
    }

    public static String norm(CharSequence seq) { return NORM.apply(seq); }

    protected SoftCache<String, String> normalizedCache = new SoftCache<>();

    protected static final Pattern ASCII = Pattern.compile("[^\\p{ASCII}]");

    protected static final Normalize NORM = new Normalize();
}
