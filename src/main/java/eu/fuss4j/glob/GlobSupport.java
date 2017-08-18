package eu.fuss4j.glob;

import java.util.regex.PatternSyntaxException;

/**
 * Based on code from <a href="http://hg.openjdk.java.net/jdk9/hs/jdk/file/b45f8cb93c6f/src/java.base/share/classes/sun/nio/fs/Globs.java">
 * OpenJDK's (jdk9) {@code Globs}</a>.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 18, 2017
 */
class GlobSupport {

    final GlobFlavor flavor;

    static final char EOL = 0;

    GlobSupport() { this(GlobFlavor.ANY); }

    GlobSupport(GlobFlavor flavor) { this.flavor = flavor == null ? GlobFlavor.ANY : flavor; }

    boolean isGlobMeta(char c) { return "\\*?[{".indexOf(c) >= 0; }

    boolean isRegexMeta(char c) { return ".^$+{[]|()".indexOf(c) >= 0; }

    char next(String glob, int i) { return 0 <= i && i < glob.length() ? glob.charAt(i) : EOL; }

    String toRegex(String glob, boolean isDos) {
        boolean inGroup = false;
        StringBuilder regex = new StringBuilder("^");

        int i = 0;
        while (i < glob.length()) {
            char c = glob.charAt(i++);
            switch (c) {
            case '\\':
                // escape special characters
                if (i == glob.length()) {
                    throw new PatternSyntaxException("No character to escape", glob, i - 1);
                }
                char next = glob.charAt(i++);
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
                if (next(glob, i) == '^') {
                    // escape the regex negation char if it appears
                    regex.append("\\^");
                    i++;
                } else {
                    // negation
                    if (next(glob, i) == '!') {
                        regex.append('^');
                        i++;
                    }
                    // hyphen allowed at start
                    if (next(glob, i) == '-') {
                        regex.append('-');
                        i++;
                    }
                }
                boolean hasRangeStart = false;
                char last = 0;
                while (i < glob.length()) {
                    c = glob.charAt(i++);
                    if (c == ']') {
                        break;
                    }
                    if (c == '/' || (isDos && c == '\\')) {
                        throw new PatternSyntaxException("Explicit 'name separator' in class", glob, i - 1);
                    }
                    // TBD: how to specify ']' in a class?
                    if (c == '\\' || c == '[' || c == '&' && next(glob, i) == '&') {
                        // escape '\', '[' or "&&" for regex class
                        regex.append('\\');
                    }
                    regex.append(c);

                    if (c == '-') {
                        if (!hasRangeStart) {
                            throw new PatternSyntaxException("Invalid range", glob, i - 1);
                        }
                        if ((c = next(glob, i++)) == EOL || c == ']') {
                            break;
                        }
                        if (c < last) {
                            throw new PatternSyntaxException("Invalid range", glob, i - 3);
                        }
                        regex.append(c);
                        hasRangeStart = false;
                    } else {
                        hasRangeStart = true;
                        last = c;
                    }
                }
                if (c != ']') {
                    throw new PatternSyntaxException("Missing ']", glob, i - 1);
                }
                regex.append("]]");
                break;
            case '{':
                if (inGroup) {
                    throw new PatternSyntaxException("Cannot nest groups", glob, i - 1);
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
                if (next(glob, i) == '*') {
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
            throw new PatternSyntaxException("Missing '}", glob, i - 1);
        }

        return regex.append('$').toString();
    }
}
