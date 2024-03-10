package de.romanamo.explorino.core;

import de.romanamo.explorino.calc.Plane;
import de.romanamo.explorino.calc.PlaneFrame;
import de.romanamo.explorino.eval.Evaluator;
import de.romanamo.explorino.eval.Mandelbrot;
import de.romanamo.explorino.color.Colorization;
import de.romanamo.explorino.color.PaletteColorization;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Point;


public class FractalModel {

    /**
     * Evaluator.
     */
    private Evaluator evaluator =
            new Mandelbrot(10);

    /**
     * Plane.
     */
    private Plane plane =
            new PlaneFrame(
                    1.0,
                    new Point(400, 400),
                    Complex.ofCartesian(4, 4),
                    Complex.ZERO,
                    new Point(16, 16));

    /**
     * Colorization.
     */
    private Colorization colorization = PaletteColorization.EXAMPLE;

    /**
     * Gets the plane.
     *
     * @return plane
     */
    public Plane getPlane() {
        return plane;
    }

    /**
     * Sets the plane.
     *
     * @param plane plane
     */
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    /**
     * Gets the evaluator.
     *
     * @return evaluator
     */
    public Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Sets the evaluator.
     *
     * @param evaluator evaluator
     */
    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * Gets the colorization.
     *
     * @return colorization
     */
    public Colorization getColorization() {
        return colorization;
    }

    /**
     * Sets the colorization.
     *
     * @param colorization colorization
     */
    public void setColorization(Colorization colorization) {
        this.colorization = colorization;
    }
}
