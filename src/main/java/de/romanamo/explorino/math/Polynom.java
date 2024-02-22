package de.romanamo.explorino.math;

import java.util.function.Function;

/**
 * Class to represent a polynomial function.
 */
public class Polynom implements Function<Complex, Complex> {

    public static Polynom EXAMPLE = new Polynom(
            Complex.ofCartesian(-1 / 2.0, -Math.sqrt(3) / 2.0),
            Complex.ofCartesian(-1 / 2.0, Math.sqrt(3) / 2.0),
            Complex.REAL);
    private Complex[] roots;

    /**
     * Constructs a Polynom using its roots.
     *
     * @param roots roots
     */
    public Polynom(Complex... roots) {
        this.roots = roots;
    }

    @Override
    public Complex apply(Complex complex) {
        Complex multiplication = Complex.REAL;
        for (Complex root : roots) {
            //multiply linear factors
            Complex factor = complex.subtract(root);
            multiplication = multiplication.multiply(factor);
        }
        return multiplication;
    }

    /**
     * Gets the roots of the polynomial.
     *
     * @return roots
     */
    public Complex[] getRoots() {
        return roots;
    }

    /**
     * Sets the roots of the polynomial
     *
     * @param roots roots
     */
    public void setRoots(Complex[] roots) {
        this.roots = roots;
    }
}
