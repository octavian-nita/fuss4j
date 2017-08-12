package eu.fuss4j.range;

import static java.lang.Integer.compare;

/**
 * Immutable range intended at least to represent, within a character sequence, where the latter matches a pattern.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public final class Range implements Comparable<Range> {

    /**
     * Range start index, inclusive.
     */
    public final int start;

    /**
     * Range end index, exclusive.
     */
    public final int end;

    /**
     * @param start the index within a char sequence where a number of characters match a pattern; inclusive
     * @param end   the index where the matching ends; exclusive
     * @throws IndexOutOfBoundsException if {@code start} is negative or greater than or equal to {@code end}
     */
    public Range(int start, int end) {
        if (start < 0) {
            throw new IndexOutOfBoundsException("Start index out of range: " + start);
        }
        if (start >= end) {
            throw new IndexOutOfBoundsException("End index lower than or equal to start index: " + end);
        }
        this.start = start;
        this.end = end;
    }

    public int length() { return end - start; }

    @Override
    public boolean equals(Object o) {
        return this == o ||
               o != null && getClass() == o.getClass() && start == ((Range) o).start && end == ((Range) o).end;
    }

    @Override
    public int compareTo(Range loc) {
        final int cmpStart = compare(start, loc.start);
        return cmpStart == 0 ? compare(end, loc.end) : cmpStart;
    }

    @Override
    public int hashCode() { return (17 * 37 + start) * 37 + end; }

    @Override
    public String toString() { return "r(" + start + "..." + end + ")"; }
}
