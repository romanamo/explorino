package de.romanamo.explorino.core.model;

import de.romanamo.explorino.calc.Plane;
import de.romanamo.explorino.calc.PlaneFrame;
import de.romanamo.explorino.eval.Evaluator;
import de.romanamo.explorino.eval.Mandelbrot;
import de.romanamo.explorino.color.Colorization;
import de.romanamo.explorino.color.PaletteColorization;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Point;


public class Model {


    private Evaluator evaluator =
            new Mandelbrot(10);

    private Plane plane =
            new PlaneFrame(
                    1.0,
                    new Point(400, 400),
                    Complex.ofCartesian(4, 4),
                    Complex.ZERO,
                    new Point(16, 16));

    private Colorization colorization = PaletteColorization.EXAMPLE;


    public Plane getPlane() {
        return plane;
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public Colorization getColorization() {
        return colorization;
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setColorization(Colorization colorization) {
        this.colorization = colorization;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
}
