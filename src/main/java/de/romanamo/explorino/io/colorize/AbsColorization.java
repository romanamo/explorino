package de.romanamo.explorino.io.colorize;

import de.romanamo.explorino.eval.Evaluation;
import javafx.scene.paint.Color;

public class AbsColorization implements Colorable {
    @Override
    public Color colorize(Evaluation evaluation) {
        double brightness = evaluation.getIteration() == evaluation.getMaxIteration() ? 1.0 : 0.0;
        double hue = Math.min(evaluation.getEnd().abs(), 2.0);

        return Color.hsb(hue * 180, 0.7, brightness);
    }
}
