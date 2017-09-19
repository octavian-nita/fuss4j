package eu.fuss4j.range;

import static java.lang.Integer.compare;

/**
 * Immutable range abstraction. Could be used, for example, to delimit a subsequence within a character sequence, etc.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public final class Range implements Comparable<Range> {

    /**
     * Range start value / index, inclusive.
     */
    public final int start;

    /**
     * Range end value / index, exclusive.
     */
    public final int end;

    /**
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

    public CharSequence sub(CharSequence charSequence) {
        return charSequence == null ? null : charSequence.subSequence(start, end);
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
               o != null && getClass() == o.getClass() && start == ((Range) o).start && end == ((Range) o).end;
    }

    @Override
    public int compareTo(Range range) {
        final int cmpStart = compare(start, range.start);
        return cmpStart == 0 ? compare(end, range.end) : cmpStart;
    }

    @Override
    public int hashCode() { return (17 * 37 + start) * 37 + end; }

    @Override
    public String toString() { return "r(" + start + "..." + end + ")"; }
}
