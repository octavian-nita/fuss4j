package eu.fuss4j.algo;

import eu.fuss4j.MatchFn;
import eu.fuss4j.rang.MatchWithRanges;
import eu.fuss4j.rang.Range;

import java.util.Locale;
import java.util.Optional;

import static eu.fuss4j.algo.SubstringMatchFn.Occurrence.ANY;
import static eu.fuss4j.misc.Normalize.norm;
import static eu.fuss4j.rang.MatchWithRanges.withRanges;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 14, 2017
 */
public class SubstringMatchFn implements MatchFn<String, String, MatchWithRanges<String>> {

    public enum Occurrence {PREFIX, SUFFIX, PREFIX_AND_SUFFIX, ANY}

    protected Occurrence occurrence;

    protected boolean caseSensitive;

    protected boolean normalizing = true;

    public SubstringMatchFn() { this(ANY, false); }

    public SubstringMatchFn(Occurrence occurrence) { this(occurrence, false); }

    public SubstringMatchFn(boolean caseSensitive) { this(ANY, caseSensitive); }

    public SubstringMatchFn(Occurrence occurrence, boolean caseSensitive) {
        this.occurrence = occurrence == null ? ANY : occurrence;
        this.caseSensitive = caseSensitive;
    }

    /**
     * Override in order to compute a more appropriate locale, rather than the {@link Locale#getDefault() default}.
     */
    protected Locale getLocale() { return Locale.getDefault(); }

    public Occurrence getOccurrence() { return occurrence; }

    public SubstringMatchFn setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
        return this;
    }

    public boolean isCaseSensitive() { return caseSensitive; }

    public SubstringMatchFn setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public boolean isNormalizing() { return normalizing; }

    public SubstringMatchFn setNormalizing(boolean normalizing) {
        this.normalizing = normalizing;
        return this;
    }

    @Override
    public Optional<MatchWithRanges<String>> match(String item, String pattern) {
        if (item == null || pattern == null) {
            return empty();
        }

        String itemStr = item;
        String pattStr = pattern;

        if (!caseSensitive) {
            final Locale locale = getLocale();
            itemStr = itemStr.toLowerCase(locale);
            pattStr = pattStr.toLowerCase(locale);
        }

        if (normalizing) {
            itemStr = norm(itemStr);
            pattStr = norm(pattStr);
        }

        MatchWithRanges<String> match = null;

        switch (occurrence) {

        case PREFIX:
            if (itemStr.startsWith(pattStr)) {
                match = withRanges(item, MAX_VALUE, new Range(0, pattern.length()));
            }
            break;

        case SUFFIX:
            if (itemStr.endsWith(pattStr)) {
                match = withRanges(item, MAX_VALUE, new Range(item.length() - pattern.length(), item.length()));
            }
            break;

        case PREFIX_AND_SUFFIX:
            if (itemStr.startsWith(pattStr) && itemStr.endsWith(pattStr)) {
                match = withRanges(item, MAX_VALUE, new Range(0, pattern.length()),
                                   new Range(item.length() - pattern.length(), item.length()));
            }
            break;

        case ANY:
            int start = itemStr.indexOf(pattStr);
            if (start >= 0) {
                match = withRanges(item, MAX_VALUE, new Range(start, start + pattern.length()));
            }
            break;
        }

        return ofNullable(match);
    }
}
