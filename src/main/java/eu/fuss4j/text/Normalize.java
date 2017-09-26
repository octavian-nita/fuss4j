package eu.fuss4j.text;

import eu.fuss4j.cache.MemoizedFn;

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
    public String apply(CharSequence seq) { return ASCII.matcher(normalize(seq, NFD)).replaceAll(""); }

    /**
     * This convenience method uses a {@link MemoizedFn memoized} {@link Normalize} implementation. (might help
     * avoid issues like <a href="https://bugs.openjdk.java.net/browse/JDK-7004714">this one</a>)
     */
    public static String norm(CharSequence seq) { return NORM.apply(seq); }

    protected static final Pattern ASCII = Pattern.compile("[^\\p{ASCII}]");

    protected static final Function<CharSequence, String> NORM = MemoizedFn.memoize(new Normalize());
}
