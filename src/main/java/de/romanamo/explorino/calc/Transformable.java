package de.romanamo.explorino.calc;

import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Point;

/**
 * Interface to represent a transform property
 * for {@link Complex} and {@link Point}.
 */
public interface Transformable {

    /**
     * Transform a point to a complex number.
     *
     * @param point point to transform
     * @return corresponding complex number
     */
    Complex transformToPlane(Point point);

    /**
     * Transforms a complex number to a point.
     *
     * @param complex complex number to transform
     * @return corresponding point
     */
    Point transformToPoint(Complex complex);
}
