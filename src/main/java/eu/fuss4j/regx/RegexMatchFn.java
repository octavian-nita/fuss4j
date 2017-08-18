package eu.fuss4j.regx;

import eu.fuss4j.MatchFn;
import eu.fuss4j.rang.MatchWithRanges;

import java.nio.file.FileSystem;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Optional.empty;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 16, 2017
 */
public class RegexMatchFn implements MatchFn<String, String, MatchWithRanges<String>> {

    @Override
    public Optional<MatchWithRanges<String>> match(String item, String pattern) {
        if (item == null || pattern == null) {
            return empty();
        }

        Pattern p = Pattern.compile(pattern);

        return empty();
    }
}
