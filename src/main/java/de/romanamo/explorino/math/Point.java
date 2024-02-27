package de.romanamo.explorino.math;

import java.util.Objects;

/**
 * Class to represent a point in 2D-Space.
 */
public class Point {

    /**
     * x-coordinate
     */
    public int x;

    /**
     * y-coordinate
     */
    public int y;

    /**
     * Constructs a point with coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }
}
