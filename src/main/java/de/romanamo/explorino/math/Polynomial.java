package de.romanamo.explorino.math;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class to represent a polynomial function.
 */
public class Polynomial implements Function<Complex, Complex> {

    private final double[] coefficients;

    /**
     * Constructs a polynom by coefficients.
     *
     * @param coefficients coefficient
     */
    public Polynomial(double... coefficients) {
        this.coefficients = coefficients;
    }

    @Override
    public Complex apply(Complex complex) {
        Complex sum = Complex.ZERO;
        for (int i = 0; i < this.coefficients.length; i++) {
            Complex summand = complex.pow(i).multiply(this.coefficients[i]);
            sum = sum.add(summand);
        }
        return sum;
    }

    /**
     * Calculates the derivative of the polynom.
     *
     * @return derivative of the polynom.
     */
    public Polynomial derivate() {
        double[] derivative = new double[Math.max(0, this.coefficients.length - 1)];
        for (int i = 1; i < this.coefficients.length; i++) {
            derivative[i - 1] = this.coefficients[i] * i;
        }
        return new Polynomial(derivative);
    }

    /**
     * Gets the degree of the polynomial (including zero coefficients).
     *
     * @return degree of the polynomial
     */
    public int getDegree() {
        return this.coefficients.length;
    }

    /**
     * Gets the coefficients.
     *
     * @return coefficient array
     */
    public double[] getCoefficients() {
        return coefficients;
    }

    @Override
    public String toString() {
        return IntStream.range(0, this.coefficients.length)
                .mapToObj(i -> String.format("%s^%d", this.coefficients[i], i))
                .collect(Collectors.joining(" + "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Polynomial polynomial = (Polynomial) o;
        return Arrays.equals(coefficients, polynomial.coefficients);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coefficients);
    }
}
