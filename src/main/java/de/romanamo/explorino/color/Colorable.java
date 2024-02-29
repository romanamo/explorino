package de.romanamo.explorino.color;

import de.romanamo.explorino.eval.Evaluation;
import javafx.scene.paint.Color;

/**
 * Interface providing a colorization for evaluations.
 */
public interface Colorable {

    /**
     * Creates a custom colorization to a given evaluation.
     *
     * @param evaluation evaluation to colorize.
     * @return color mapping
     */
    Color colorize(Evaluation evaluation);
}
