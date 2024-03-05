package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Lyapunov extends Evaluator {

    private int[] sequence;

    public Lyapunov(int maxIteration, int[] sequence) {
        super(maxIteration);
        this.sequence = sequence;
    }

    /**
     * Extracts an array with index corresponding to alphabetic Sequences.
     * Cleans unwanted characters.
     *
     * @param sequence alphabetic sequence
     * @return indices array
     */
    public static int[] sequenceToIndices(String sequence) {
        //remove unwanted characters, convert to uppercase
        String preprocessed = sequence.replaceAll("[^a-zA-Z]", "").toUpperCase();

        //map to corresponding indices
        return preprocessed.chars().map(c -> c - 65).toArray();
    }

    /**
     * Extracts an uppercase alphabetic sequence string from corresponding indices.
     *
     * @param indices indices array
     * @return sequence string
     */
    public static String indicesToSequence(int[] indices) {
        return Arrays.stream(indices)
                .mapToObj(c -> String.valueOf((char) (c + 65)))
                .collect(Collectors.joining());
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
        throw new UnsupportedOperationException("Unused function");
    }
}
