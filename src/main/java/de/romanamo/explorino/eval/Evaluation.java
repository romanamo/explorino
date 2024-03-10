package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

/**
 * Class to represent the result of an evaluation.
 */
public class Evaluation {

    /**
     * Input of the evaluation.
     */
    private final Complex start;

    /**
     * Output of the evaluation.
     */
    private final Complex end;

    /**
     * Reached Iteration.
     */
    private final int iteration;

    /**
     * Maximum Iteration.
     */
    private final int maxIteration;

    /**
     * Constructs an Evaluation.
     *
     * @param start        input
     * @param end          output
     * @param iteration    reached iteration
     * @param maxIteration maximum iteration
     */

    public Evaluation(Complex start, Complex end, int iteration, int maxIteration) {
        this.start = start;
        this.end = end;
        this.iteration = iteration;
        this.maxIteration = maxIteration;
    }

    /**
     * Gets the start input.
     *
     * @return start input
     */
    public Complex getStart() {
        return start;
    }

    /**
     * Gets the end output.
     *
     * @return end output
     */
    public Complex getEnd() {
        return end;
    }

    /**
     * Gets the reached iteration.
     *
     * @return reached iteration
     */
    public int getIteration() {
        return iteration;
    }

    /**
     * Gets the maximum iteration.
     *
     * @return maximum iteration
     */
    public int getMaxIteration() {
        return maxIteration;
    }
}
