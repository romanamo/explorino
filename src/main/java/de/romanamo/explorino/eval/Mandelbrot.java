package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;

public class Mandelbrot extends Evaluator {
    public Mandelbrot(int maxIteration) {
        super(maxIteration);
    }

    @Override
    public Function<Complex, Complex> function(Complex element) {
        return c -> c.multiply(c).add(element);
    }

    @Override
    public Complex initial(Complex element) {
        return Complex.ZERO;
    }
}
