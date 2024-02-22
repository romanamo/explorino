package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;

public class Polynomial extends Evaluator {

    private final Complex parameter;
    private final int degree;

    public Polynomial(int maxIteration, int degree, Complex parameter) {
        super(maxIteration, c -> c.distance(Complex.ZERO) > 2);
        this.degree = degree;
        this.parameter = parameter;
    }

    @Override
    public Function<Complex, Complex> function(Complex element) {
        return (c) -> c.pow(degree).add(parameter);
    }

    @Override
    public Complex initial(Complex element) {
        return element;
    }

    public Complex getParameter() {
        return parameter;
    }

    public int getDegree() {
        return degree;
    }
}
