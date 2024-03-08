package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;

public class MultiBrot extends Evaluator{

    private int degree;

    /**
     * Constructs the Mandelbrot fractal.
     *
     * @param maxIteration maximum iteration of the evaluating process
     */
    public MultiBrot(int maxIteration, int degree) {
        super(maxIteration);
        this.degree = degree;
    }

    @Override
    public Function<Complex, Complex> function(Complex element) {
        return c -> c.pow(degree).add(element);
    }

    @Override
    public Complex initial(Complex element) {
        return Complex.ZERO;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
