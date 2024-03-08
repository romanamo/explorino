package de.romanamo.explorino.color;

import de.romanamo.explorino.eval.Evaluation;
import javafx.scene.paint.Color;

public class ArgColorization extends InvertibleColorization {


    public ArgColorization(boolean inverted) {
        super(inverted);
    }

    @Override
    public Color colorize(Evaluation evaluation) {
        double argument = evaluation.getEnd().argument();

        double brightness = evaluation.getIteration() == evaluation.getMaxIteration() ? 1.0 : 0.0;
        brightness = this.inverted ? 1 - brightness : brightness;

        double color = argument * 360 / Math.PI;

        return Color.hsb(color, 0.7, brightness);
    }

}
