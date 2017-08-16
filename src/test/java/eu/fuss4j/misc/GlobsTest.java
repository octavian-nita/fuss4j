package eu.fuss4j.misc;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 16, 2017
 */
public class GlobsTest {

    private static final String[] GLOBS = { //@fmt:off
        "{sun,moon,stars}",
        "{temp*,tmp*}",
        "[aeiou]",
        "[0-9]",
        "[A-Z]",
        "[a-z,A-Z]",
        "\\\\",
        "\\?",
        "*.html",
        "???",
        "*[0-9]*",
        "*.{htm,html,pdf}",
        "a?*.java",
        "{foo*,*[0-9]*}",
        "*.java",
        "*.*",
        "*.{java,class}",
        "foo.?",
        "/home/*/*",
        "/home/**",
        "C:\\*" //@fmt:on
    };

    @Test
    public void miscGlobsMatchesSunGlobs()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final Class<?> klass = sun.nio.fs.Globs.class;

        final Method toUnixRegexPatternM = klass.getDeclaredMethod("toUnixRegexPattern", String.class);
        toUnixRegexPatternM.setAccessible(true);

        final Method toWindowsRegexPatternM = klass.getDeclaredMethod("toWindowsRegexPattern", String.class);
        toWindowsRegexPatternM.setAccessible(true);

        for (String glob : GLOBS) {
            assertEquals(toUnixRegexPatternM.invoke(null, glob), Globs.toUnixRegexPattern(glob));
            assertEquals(toWindowsRegexPatternM.invoke(null, glob), Globs.toWindowsRegexPattern(glob));
            Pattern.compile(Globs.toRegexPattern(glob)); // check that the generic regex compiles at least...
        }
    }
}
