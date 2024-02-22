package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Numeric;

import java.util.function.Function;

public class Newton extends Evaluator {

    private Function<Complex, Complex> function;

    public Newton(int maxIteration, Function<Complex, Complex> function) {
        super(maxIteration);
        this.function = function;
        //TODO implement Nova fractal
    }

    @Override
    public Function<Complex, Complex> function(Complex element) {
        return (z) -> {
            Complex derivative = Numeric.differentiate(this.function, z, Complex.ofCartesian(1e-5, 1e-5));

            Complex fraction = this.function.apply(z).divide(derivative);

            return z.subtract(fraction);
        };

    }

    @Override
    public Evaluation evaluate(Complex element) {
        //set start values for the evaluating process
        int iteration = 0;
        Complex num = this.initial(element);
        Function<Complex, Complex> function = this.function(element);

        for (int i = 0; i < this.getMaxIteration(); i++) {
            num = function.apply(num);
        }
        return new Evaluation(element, num, iteration, this.getMaxIteration());
    }

    @Override
    public Complex initial(Complex element) {
        return element;
    }
}
