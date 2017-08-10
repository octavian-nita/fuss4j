package eu.fuss4j;

import static java.lang.Integer.compare;

/**
 * Location within a character sequence where a subsequence in a pattern is found.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 9, 2017
 */
public class Location implements Comparable<Location> {

    public final int start;

    public final int end;

    /**
     * @param start the index within a char sequence where ({@code end - start}) characters in a pattern are found
     *              sequentially; inclusive
     * @param end   the index where the matching subsequence ends; exclusive
     * @throws IndexOutOfBoundsException if {@code start} is negative or greater than or equal to {@code end}
     */
    public Location(int start, int end) {
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
               o != null && getClass() == o.getClass() && start == ((Location) o).start && end == ((Location) o).end;
    }

    @Override
    public int compareTo(Location loc) {
        final int cmpStart = compare(start, loc.start);
        return cmpStart == 0 ? compare(end, loc.end) : cmpStart;
    }

    @Override
    public int hashCode() { return (17 * 37 + start) * 37 + end; }

    @Override
    public String toString() { return "loc[" + start + "..." + end + "]"; }
}
