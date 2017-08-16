package eu.fuss4j.misc;

import java.util.regex.PatternSyntaxException;

/**
 * Taken (and slightly adapted) from <a href="http://hg.openjdk.java.net/jdk9/hs/jdk/file/b45f8cb93c6f/src/java.base/share/classes/sun/nio/fs/Globs.java">
 * OpenJDK's (jdk9) {@code Globs}</a>.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 16, 2017
 */
public class Globs {

    private Globs() {}

    private static final String regexMetaChars = ".^$+{[]|()";

    private static boolean isRegexMeta(char c) { return regexMetaChars.indexOf(c) != -1; }

    private static final String globMetaChars = "\\*?[{";

    private static boolean isGlobMeta(char c) { return globMetaChars.indexOf(c) != -1; }

    private static final char EOL = 0;

    private static char next(String glob, int i) { return i < glob.length() ? glob.charAt(i) : EOL; }

    private static String toRegexPattern(String globPattern, boolean isDos) {
        boolean inGroup = false;
        StringBuilder regex = new StringBuilder("^");

        int i = 0;
        while (i < globPattern.length()) {
            char c = globPattern.charAt(i++);
            switch (c) {
            case '\\':
                // escape special characters
                if (i == globPattern.length()) {
                    throw new PatternSyntaxException("No character to escape", globPattern, i - 1);
                }
                char next = globPattern.charAt(i++);
                if (isGlobMeta(next) || isRegexMeta(next)) {
                    regex.append('\\');
                }
                regex.append(next);
                break;
            case '/':
                if (isDos) {
                    regex.append("\\\\");
                } else {
                    regex.append(c);
                }
                break;
            case '[':
                // don't match name separator in class
                if (isDos) {
                    regex.append("[[^\\\\]&&[");
                } else {
                    regex.append("[[^/]&&[");
                }
                if (next(globPattern, i) == '^') {
                    // escape the regex negation char if it appears
                    regex.append("\\^");
                    i++;
                } else {
                    // negation
                    if (next(globPattern, i) == '!') {
                        regex.append('^');
                        i++;
                    }
                    // hyphen allowed at start
                    if (next(globPattern, i) == '-') {
                        regex.append('-');
                        i++;
                    }
                }
                boolean hasRangeStart = false;
                char last = 0;
                while (i < globPattern.length()) {
                    c = globPattern.charAt(i++);
                    if (c == ']') {
                        break;
                    }
                    if (c == '/' || (isDos && c == '\\')) {
                        throw new PatternSyntaxException("Explicit 'name separator' in class", globPattern, i - 1);
                    }
                    // TBD: how to specify ']' in a class?
                    if (c == '\\' || c == '[' || c == '&' && next(globPattern, i) == '&') {
                        // escape '\', '[' or "&&" for regex class
                        regex.append('\\');
                    }
                    regex.append(c);

                    if (c == '-') {
                        if (!hasRangeStart) {
                            throw new PatternSyntaxException("Invalid range", globPattern, i - 1);
                        }
                        if ((c = next(globPattern, i++)) == EOL || c == ']') {
                            break;
                        }
                        if (c < last) {
                            throw new PatternSyntaxException("Invalid range", globPattern, i - 3);
                        }
                        regex.append(c);
                        hasRangeStart = false;
                    } else {
                        hasRangeStart = true;
                        last = c;
                    }
                }
                if (c != ']') {
                    throw new PatternSyntaxException("Missing ']", globPattern, i - 1);
                }
                regex.append("]]");
                break;
            case '{':
                if (inGroup) {
                    throw new PatternSyntaxException("Cannot nest groups", globPattern, i - 1);
                }
                regex.append("(?:(?:");
                inGroup = true;
                break;
            case '}':
                if (inGroup) {
                    regex.append("))");
                    inGroup = false;
                } else {
                    regex.append('}');
                }
                break;
            case ',':
                if (inGroup) {
                    regex.append(")|(?:");
                } else {
                    regex.append(',');
                }
                break;
            case '*':
                if (next(globPattern, i) == '*') {
                    // crosses directory boundaries
                    regex.append(".*");
                    i++;
                } else {
                    // within directory boundary
                    if (isDos) {
                        regex.append("[^\\\\]*");
                    } else {
                        regex.append("[^/]*");
                    }
                }
                break;
            case '?':
                if (isDos) {
                    regex.append("[^\\\\]");
                } else {
                    regex.append("[^/]");
                }
                break;

            default:
                if (isRegexMeta(c)) {
                    regex.append('\\');
                }
                regex.append(c);
            }
        }

        if (inGroup) {
            throw new PatternSyntaxException("Missing '}", globPattern, i - 1);
        }

        return regex.append('$').toString();
    }

    public static String toRegexPattern(String globPattern) {
        final String unixRegexPattern = toUnixRegexPattern(globPattern);
        final String windowsRegexPattern = toWindowsRegexPattern(globPattern);
        return unixRegexPattern.equals(windowsRegexPattern) ? unixRegexPattern
                                                            : ("(?:" + toUnixRegexPattern(globPattern) + ")|(?:" +
                                                               toWindowsRegexPattern(globPattern) + ")");
    }

    public static String toUnixRegexPattern(String globPattern) { return toRegexPattern(globPattern, false); }

    public static String toWindowsRegexPattern(String globPattern) { return toRegexPattern(globPattern, true); }
}
