package de.romanamo.explorino.color;

import de.romanamo.explorino.eval.Evaluation;
import javafx.scene.paint.Color;

import java.util.List;

public class PaletteColorization extends Colorization {

    public static final PaletteColorization EXAMPLE = new PaletteColorization(
            List.of(
                    Color.rgb(9, 1, 47),
                    Color.rgb(4, 4, 73),
                    Color.rgb(0, 7, 100),
                    Color.rgb(12, 44, 138),
                    Color.rgb(24, 82, 177),
                    Color.rgb(57, 125, 209),
                    Color.rgb(134, 181, 229),
                    Color.rgb(211, 236, 248),
                    Color.rgb(241, 233, 191),
                    Color.rgb(248, 201, 95),
                    Color.rgb(255, 170, 0),
                    Color.rgb(204, 128, 0),
                    Color.rgb(153, 87, 0),
                    Color.rgb(106, 52, 3)));

    private final List<Color> palette;

    private boolean interpolated;

    public PaletteColorization(List<Color> palette) {
        this.palette = palette;
        this.interpolated = true;
    }

    private Color retrieve(int i) {
        return this.palette.get(i % this.palette.size());
    }

    @Override
    public Color colorize(Evaluation evaluation) {
        if (evaluation.getIteration() < evaluation.getMaxIteration()) {
            int i = evaluation.getIteration();
            if (interpolated) {
                double ratio = (double) evaluation.getIteration() / evaluation.getMaxIteration();
                double x = (Math.cos(ratio * Math.PI + Math.PI) + 1.0) / 2.0;

                return this.retrieve((int) (x * this.palette.size() * 3));
            } else {
                return this.retrieve(i);
            }

        }
        return Color.hsb(0, 0, 0);
    }
}
