package de.romanamo.explorino.math;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Class to represent a polynomial function.
 */
public class Polynom implements Function<Complex, Complex> {

    private double[] coefficients;

    public Polynom(double... coefficients) {
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

    public double[] getCoefficients() {
        return Arrays.copyOf(this.coefficients, this.coefficients.length);
    }

    public Polynom derivate() {
        double[] derivative = new double[this.coefficients.length-1];
        for (int i = 1; i < this.coefficients.length; i++) {
            derivative[i - 1] = this.coefficients[i] * i;
        }
        return new Polynom(derivative);
    }
}
