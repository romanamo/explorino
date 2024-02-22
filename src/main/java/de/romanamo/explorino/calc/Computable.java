package de.romanamo.explorino.calc;

import de.romanamo.explorino.eval.Evaluator;

/**
 * Class to represent the property to compute a fractal.
 */
public interface Computable {

    /**
     * Computes a fractal.
     *
     * @param evaluator fractal
     * @return result grid
     */
    Grid compute(Evaluator evaluator);
}
