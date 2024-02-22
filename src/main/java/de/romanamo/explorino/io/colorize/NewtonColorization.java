package de.romanamo.explorino.io.colorize;

import de.romanamo.explorino.eval.Evaluation;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Polynom;
import javafx.scene.paint.Color;

public class NewtonColorization implements Colorable {

    private Polynom polynom;
    public NewtonColorization(Polynom polynom) {
        this.polynom = polynom;
    }
    @Override
    public Color colorize(Evaluation evaluation) {
        Complex zn = evaluation.getEnd();

        Complex[] roots = this.polynom.getRoots();

        int closestIndex = 0;

        for (int i = 0; i < roots.length; i++) {
            if(zn.distance(roots[i]) < zn.distance(roots[closestIndex])) {
                closestIndex = i;
            }
        }
        return Color.hsb(360 * closestIndex / (double) roots.length, 0.7, 1);
    }

    public void setPolynom(Polynom polynom) {
        this.polynom = polynom;
    }

    public Polynom getPolynom() {
        return polynom;
    }
}
