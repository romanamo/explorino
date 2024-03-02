package de.romanamo.explorino.math;

import java.util.function.Function;

/**
 * Class containing useful methods for numeric approximations.
 */
public final class Numeric {

    /**
     * Approximates the difference quotient for a complex valued function on x,
     * using a sufficiently small delta h.
     *
     * @param function function
     * @param x        argument
     * @param h        delta
     * @return approximated difference qoutient
     */
    public static Complex differentiate(Function<Complex, Complex> function, Complex x, Complex h) {
        Complex addedH = function.apply(x.add(h));
        Complex withoutH = function.apply(x);
        Complex numerator = addedH.subtract(withoutH);
        return numerator.divide(h);
    }

    /**
     * Divides and rounds resulting fraction to next bigger long value.
     *
     * @param dividend dividend
     * @param divisor  divisor
     * @return ceil(dividend / divisor)
     */
    public static long ceilDiv(long dividend, long divisor) {
        return -Math.floorDiv(-dividend, divisor);
    }

    public static double clamp(double x, double min, double max) {
        if(x <= min) {
            return min;
        } else if (x >= max) {
            return max;
        }
        return x;
    }
}
