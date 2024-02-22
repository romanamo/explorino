package de.romanamo.explorino.io.colorize;

import de.romanamo.explorino.eval.Evaluation;
import javafx.scene.paint.Color;

public class BasicColorization implements Colorable {

    @Override
    public Color colorize(Evaluation evaluation) {
        double ratio = (double) evaluation.getIteration() / evaluation.getMaxIteration();
        double brightness = evaluation.getIteration() >= evaluation.getMaxIteration() ? 0.0 : 1.0;

        return Color.hsb(ratio * 360, 0.7, brightness);
    }
}
