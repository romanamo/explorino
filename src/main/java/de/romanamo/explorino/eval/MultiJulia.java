package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;

public class MultiJulia extends Evaluator {

    private Complex parameter;
    private int degree;

    public MultiJulia(int maxIteration, int degree, Complex parameter) {
        super(maxIteration);
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

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setParameter(Complex parameter) {
        this.parameter = parameter;
    }

}
