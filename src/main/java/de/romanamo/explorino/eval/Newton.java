package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Polynomial;

import java.util.function.Function;

/**
 * Class to represent the newton fractal.
 */
public class Newton extends Evaluator {

    private final double TOLERANCE = 1e-5;
    private Polynomial polynomial;

    private Polynomial derivative;

    /**
     * Constructs a Newton fractal.
     *
     * @param maxIteration maximum iteration of the evaluating process
     * @param polynomial      function to evaluate
     */
    public Newton(int maxIteration, Polynomial polynomial) {
        super(maxIteration);
        this.polynomial = polynomial;
        this.derivative = polynomial.derivate();
    }

    @Override
    public Function<Complex, Complex> function(Complex element) {
        return (z) -> {
            Complex derivative = this.derivative.apply(z);
            Complex fraction = this.polynomial.apply(z).divide(derivative);

            return z.subtract(fraction);
        };

    }

    @Override
    public Evaluation evaluate(Complex element) {
        //set start values for the evaluating process
        int iteration = 0;
        Function<Complex, Complex> function = this.function(element);

        Complex current = this.initial(element);
        Complex next = function.apply(current);

        while (iteration < this.getMaxIteration() && current.distanceSquared(next) > TOLERANCE) {
            current = next;
            next = function.apply(next);
            iteration++;
        }
        return new Evaluation(element, next, iteration, this.getMaxIteration());
    }

    @Override
    public Complex initial(Complex element) {
        return element;
    }

    public Polynomial getPolynom() {
        return polynomial;
    }

    public Polynomial getDerivative() {
        return derivative;
    }

    public void setPolynom(Polynomial polynomial) {
        this.polynomial = polynomial;
    }

    public void setDerivative(Polynomial derivative) {
        this.derivative = derivative;
    }
}
