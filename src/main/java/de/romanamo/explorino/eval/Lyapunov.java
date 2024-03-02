package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;

public class Lyapunov extends Evaluator {

    private int[] sequence;

    public Lyapunov(int maxIteration, int[] sequence) {
        super(maxIteration);
        this.sequence = sequence;
    }

    private double resolve(int i, double[] values) {
        int valueIndex = sequence[i % sequence.length];

        return values[valueIndex % values.length];
    }

    private double logistic(double x, double r) {
        return r * x * (1 - x);
    }

    @Override
    public Evaluation evaluate(Complex element) {
        double[] point = element.toArray();
        double x = 0.5;
        double lambda = 1.0;

        for (int i = 0; i < this.getMaxIteration(); i++) {
            double r = resolve(i, point);

            x = logistic(x, r);
            
            lambda *= Math.abs(r * (1 - 2 * x));
        }
        lambda = Math.log(lambda) / this.getMaxIteration();

        return new Evaluation(
                element,
                Complex.ofCartesian(lambda, 0),
                this.getMaxIteration(),
                this.getMaxIteration());
    }

    public void setSequence(int[] sequence) {
        this.sequence = sequence;
    }

    public int[] getSequence() {
        return sequence;
    }

    @Override
    public Function<Complex, Complex> function(Complex element) {
        throw new UnsupportedOperationException("Unused function");
    }

    @Override
    public Complex initial(Complex element) {
        return element;
    }
}
