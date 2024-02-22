package de.romanamo.explorino.io.colorize;

import de.romanamo.explorino.eval.Evaluation;
import javafx.scene.paint.Color;

public class ArgColorization implements Colorable {

    private boolean inverted;

    public ArgColorization(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public Color colorize(Evaluation evaluation) {
        double argument = evaluation.getEnd().argument();

        double brightness = evaluation.getIteration() == evaluation.getMaxIteration() ? 1.0 : 0.0;
        brightness = inverted ? 1 - brightness : brightness;

        double color = argument * 360 / Math.PI;

        return Color.hsb(color, 0.7, brightness);
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean getInverted() {
        return this.inverted;
    }


}
