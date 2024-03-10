package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;

/**
 * Class to represent the Mandelbrot Fractal.
 */
public class Mandelbrot extends Evaluator {

    /**
     * Constructs the Mandelbrot fractal.
     *
     * @param maxIteration maximum iteration of the evaluating process
     */
    public Mandelbrot(int maxIteration) {
        super(maxIteration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Complex, Complex> function(Complex element) {
        return c -> c.multiply(c).add(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Complex initial(Complex element) {
        return Complex.ZERO;
    }
}
