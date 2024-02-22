package de.romanamo.explorino.math;

import java.util.Objects;

/**
 * Class to represent complex numbers.
 */
public class Complex {

    public static final Complex ZERO = new Complex(0, 0);

    public static final Complex REAL = new Complex(1, 0);

    public static final Complex IMAGINARY = new Complex(0, 1);

    private double real;

    private double imaginary;

    /**
     * Constructs a complex number.
     *
     * @param real      real part
     * @param imaginary imaginary part
     */
    private Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Constructs a complex number using its cartesian form.
     *
     * @param real      real part
     * @param imaginary imaginary part
     * @return complex number
     */
    public static Complex ofCartesian(double real, double imaginary) {
        return new Complex(real, imaginary);
    }

    /**
     * Constructs a complex number using its polar form.
     *
     * @param angle  angle in radians
     * @param radius positive radius
     * @return complex number
     */
    public static Complex ofPolar(double angle, double radius) {
        double absRadius = Math.abs(radius);
        return new Complex(absRadius * Math.cos(angle), absRadius * Math.sin(angle));
    }

    /**
     * Adds complex numbers a,b together.
     *
     * @param summand summand to add
     * @return a+b
     */
    public Complex add(Complex summand) {
        return new Complex(this.real + summand.real, this.imaginary + summand.imaginary);
    }

    /**
     * Subtracts a complex numbers.
     *
     * @param subtrahend subtrahend
     * @return subtraction
     */
    public Complex subtract(Complex subtrahend) {
        return new Complex(this.real - subtrahend.real, this.imaginary - subtrahend.imaginary);
    }

    /**
     * Rotates the complex number c by a given angle.
     *
     * @param angle angle in radians
     * @return rotated c
     */
    public Complex rotate(double angle) {
        //apply rotation matrix for R^2
        double rotatedReal = this.real * Math.cos(angle) - this.imaginary * Math.sin(angle);
        double rotatedImaginary = this.real * Math.sin(angle) + this.imaginary * Math.cos(angle);

        return new Complex(rotatedReal, rotatedImaginary);
    }

    /**
     * Multiplies a complex number c with a scalar s.
     *
     * @param multiplicand scalar multiplicand
     * @return s*c
     */
    public Complex multiply(double multiplicand) {
        return new Complex(this.real * multiplicand, this.imaginary * multiplicand);
    }

    /**
     * Multiplies 2 complex numbers c,d.
     *
     * @param multiplicand complex multiplicand
     * @return c*d
     */
    public Complex multiply(Complex multiplicand) {
        double multipliedReal = this.real * multiplicand.real - this.imaginary * multiplicand.imaginary;
        double multipliedImaginary = this.imaginary * multiplicand.real + multiplicand.imaginary * this.real;

        return new Complex(multipliedReal, multipliedImaginary);
    }

    /**
     * Divides a complex number c by a real divisor d.
     *
     * @param divisor real divisor
     * @return c/d
     */
    public Complex divide(double divisor) {
        return new Complex(this.real / divisor, this.imaginary / divisor);
    }

    /**
     * Divides a complex number c by a complex divisor d.
     *
     * @param divisor complex divisor
     * @return the number c/d
     */
    public Complex divide(Complex divisor) {
        if (divisor.equals(Complex.ZERO)) {
            throw new ArithmeticException(String.format("Can not divide %s by zero", this));
        }
        double dividedRadius = this.getRadius() / divisor.getRadius();
        double subtractedAngle = this.getAngle() - divisor.getAngle();

        double dividedReal = dividedRadius * Math.cos(subtractedAngle);
        double dividedImaginary = dividedRadius * Math.sin(subtractedAngle);

        return new Complex(dividedReal, dividedImaginary);
    }

    /**
     * Calculates the Euclidean distance between 2 complex numbers a,b.
     *
     * @param other other
     * @return distance between a, b
     */
    public double distance(Complex other) {
        double realDifference = this.real - other.real;
        double imaginaryDifference = this.imaginary - other.imaginary;

        return Math.sqrt(realDifference * realDifference + imaginaryDifference * imaginaryDifference);
    }

    /**
     * Calculates the distance to the origin.
     *
     * @return distance to the origin
     */
    public double abs() {
        return this.distance(Complex.ZERO);
    }

    /**
     * Raises a complex number c to an integer exponent n using
     * <a href="https://en.wikipedia.org/wiki/De_Moivre%27s_formula">De Moivre's Theorem</a>.
     *
     * @param exponent exponent
     * @return the number c<sup>n</sup>
     */
    public Complex pow(int exponent) {
        //Check simple cases via switch statement to increase performance
        switch (exponent) {
            case 0:
                return Complex.REAL;
            case 1:
                return this;
            case 2:
                return this.multiply(this);
            default:
                //do expensive calculation
                double poweredRadius = Math.pow(this.getRadius(), exponent);
                double multipliedAngle = exponent * this.getAngle();

                double poweredReal = poweredRadius * Math.cos(multipliedAngle);
                double poweredImaginary = poweredRadius * Math.sin(multipliedAngle);

                return new Complex(poweredReal, poweredImaginary);
        }
    }

    /**
     * Changes the sign of the imaginary part of a number c.
     *
     * @return conj(c)
     */
    public Complex conjugate() {
        return new Complex(this.real, -this.imaginary);
    }


    /**
     * Calculates the argument of the complex number.
     *
     * @return arg(c)
     */
    public double argument() {
        return Math.atan(this.imaginary / this.real);
    }

    /**
     * Changes a complex number c to specified quadrant of the complex plane by changing its signs.
     * <p>
     * <code>True</code> := positive sign<br>
     * <code>False</code> := negative sign
     * </p>
     *
     * @param real      real specification
     * @param imaginary imaginary specification
     * @return complex number in specified quadrant
     */
    public Complex toQuadrant(boolean real, boolean imaginary) {
        double absReal = Math.abs(this.real);
        double absImag = Math.abs(this.imaginary);
        //create complex number in quadrant via conditions
        return new Complex(real ? absReal : -absReal, imaginary ? absImag : -absImag);
    }

    /**
     * Gets the angle of a complex number. Needed for polar form.
     *
     * @return angle in radians
     */
    public double getAngle() {
        return Math.atan2(this.imaginary, this.real);
    }

    /**
     * Gets the radius of a complex number. Needed for polar form.
     *
     * @return radius
     */
    public double getRadius() {
        return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
    }

    /**
     * Gets the real part of a complex number. Needed for cartesian form.
     *
     * @return real part
     */
    public double getReal() {
        return real;
    }

    /**
     * Gets the imaginary part of a complex number. Needed for cartesian form.
     *
     * @return imaginary part
     */
    public double getImaginary() {
        return imaginary;
    }


    /**
     * Sets the angle of the complex number. As specified in polar form.
     *
     * @param angle angle in radians
     */
    public void setAngle(double angle) {
        double oldRadius = this.getRadius();

        this.real = oldRadius * Math.cos(angle);
        this.imaginary = oldRadius * Math.sin(angle);
    }

    /**
     * Sets the radius of the complex number. As specified in polar form.
     *
     * @param radius positive radius
     */
    public void setRadius(double radius) {
        double oldAngle = this.getAngle();
        double absRadius = Math.abs(radius);

        this.real = absRadius * Math.cos(oldAngle);
        this.imaginary = absRadius * Math.sin(oldAngle);
    }

    /**
     * Sets the real part of a complex number. As specified in cartesian form.
     *
     * @param real real part
     */
    public void setReal(double real) {
        this.real = real;
    }

    /**
     * Sets the imaginary part of a complex number. As specified in cartesian form.
     *
     * @param imaginary imaginary part
     */
    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    @Override
    public String toString() {
        return String.format("%s + %si", this.real, this.imaginary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(real, complex.real) == 0 && Double.compare(imaginary, complex.imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }
}
