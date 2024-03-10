package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;

public class MultiBrot extends Evaluator {

    /**
     * Degree.
     */
    private int degree;

    /**
     * Constructs the MultiBrot fractal.
     *
     * @param maxIteration maximum iteration of the evaluating process
     * @param degree       degree
     */
    public MultiBrot(int maxIteration, int degree) {
        super(maxIteration);
        this.degree = degree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Complex, Complex> function(Complex element) {
        return c -> c.pow(degree).add(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Complex initial(Complex element) {
        return Complex.ZERO;
    }

    /**
     * Gets the degree.
     *
     * @return degree.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Sets the degree.
     *
     * @param degree degree
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }
}
