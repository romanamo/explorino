package de.romanamo.explorino.core.model;

import de.romanamo.explorino.calc.Plane;
import de.romanamo.explorino.calc.PlaneFrame;
import de.romanamo.explorino.eval.Evaluator;
import de.romanamo.explorino.eval.Mandelbrot;
import de.romanamo.explorino.io.colorize.Colorable;
import de.romanamo.explorino.io.colorize.PaletteColorization;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Point;


public class Model {


    private Evaluator evaluator =
            new Mandelbrot(10);

    private Plane plane =
            new PlaneFrame(
                    1.0,
                    new Point(600, 600),
                    Complex.ofCartesian(4, 4),
                    Complex.ZERO,
                    new Point(16, 16));

    private Colorable colorization = PaletteColorization.EXAMPLE;


    public Plane getPlane() {
        return plane;
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public Colorable getColorization() {
        return colorization;
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setColorization(Colorable colorization) {
        this.colorization = colorization;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
}
