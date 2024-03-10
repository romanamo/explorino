package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;

public class MultiJulia extends Evaluator {

    /**
     * Parameter.
     */
    private Complex parameter;

    /**
     * Degree.
     */
    private int degree;

    /**
     * Constructs a {@link MultiJulia} Fractal.
     *
     * @param maxIteration maximum iteration of the evaluating process
     * @param degree       degree
     * @param parameter    parameter
     */
    public MultiJulia(int maxIteration, int degree, Complex parameter) {
        super(maxIteration);
        this.degree = degree;
        this.parameter = parameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Complex, Complex> function(Complex element) {
        return (c) -> c.pow(degree).add(parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Complex initial(Complex element) {
        return element;
    }

    /**
     * Gets the parameter.
     *
     * @return parameter
     */
    public Complex getParameter() {
        return parameter;
    }

    /**
     * Sets the parameter.
     *
     * @param parameter parameter
     */
    public void setParameter(Complex parameter) {
        this.parameter = parameter;
    }

    /**
     * Gets the degree.
     *
     * @return degree
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
