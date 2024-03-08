package de.romanamo.explorino.color;

import de.romanamo.explorino.eval.Evaluation;
import javafx.scene.paint.Color;

/**
 * Class representing a colorization for evaluations.
 */
public abstract class Colorization {

    /**
     * Creates a custom colorization to a given evaluation.
     *
     * @param evaluation evaluation to colorize.
     * @return color mapping
     */
    public abstract Color colorize(Evaluation evaluation);

    /**
     * Gets the name of the colorization.
     *
     * @return name of the colorization
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
