package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class to represent a Fractal.
 */
public abstract class Evaluator {

    private int maxIteration;

    private Predicate<Complex> escapeCondition;

    /**
     * Constructs an Evaluator.
     *
     * @param maxIteration    maximum iteration of the evaluating process
     * @param escapeCondition determines if a point escaped
     */
    public Evaluator(int maxIteration, Predicate<Complex> escapeCondition) {
        this.setMaxIteration(maxIteration);
        this.setEscapeCondition(escapeCondition);
    }

    /**
     * Construct an Evaluator with escape distance of 2.
     *
     * @param maxIteration maximum iteration of the evaluating process
     */
    public Evaluator(int maxIteration) {
        this.setMaxIteration(maxIteration);
        this.setEscapeCondition(c -> c.distanceSquared(Complex.ZERO) > 4);

    }

    /**
     * Creates an {@link Evaluation} for a complex number,
     * determining how the complex number behaves in the process of continuously
     * applying a function maxIteration times.
     * <p>
     * <code>z<sub>n+1</sub> = f(z<sub>n</sub>)</code>
     * </p>
     *
     * @param element complex number to evaluate
     * @return evaluation of given complex number
     */
    public Evaluation evaluate(Complex element) {
        //set start values for the evaluating process
        int iteration = 0;
        Complex num = this.initial(element);
        Function<Complex, Complex> function = this.function(element);

        //return if maxIteration has been reached or the escapeCondition has been met
        while (!this.getEscapeCondition().test(num) && iteration < this.maxIteration) {
            //pass through iteration by applying function
            num = function.apply(num);
            iteration++;
        }
        return new Evaluation(element, num, iteration, this.maxIteration);
    }

    /**
     * Retrieves the function for the iterating process.
     *
     * @param element complex number to evaluate
     * @return iterating function
     */
    public abstract Function<Complex, Complex> function(Complex element);

    /**
     * Retrieves the initial element of the iterating process.
     *
     * @param element complex number to evaluate
     * @return initial element
     */
    public abstract Complex initial(Complex element);

    /**
     * Determines if a complex number is inside the described set.
     *
     * @param element complex number to evaluate
     * @return {@code true} if inside the set, otherwise {@code false}
     */
    public boolean inSet(Complex element) {
        return this.getMaxIteration() == this.evaluate(element).getIteration();
    }


    public int getMaxIteration() {
        return this.maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        if (maxIteration < 0) {
            throw new IllegalArgumentException(
                    String.format("Maximum Iteration: %d has to be greater or equals 0", maxIteration));
        }
        this.maxIteration = maxIteration;
    }

    public Predicate<Complex> getEscapeCondition() {
        return this.escapeCondition;
    }

    public void setEscapeCondition(Predicate<Complex> escapeCondition) {
        this.escapeCondition = escapeCondition;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
