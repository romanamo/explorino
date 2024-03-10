package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MandelbrotTest {

    @Test
    public void insideSet() {
        Mandelbrot mandelbrot = new Mandelbrot(100);

        assertTrue(mandelbrot.inSet(Complex.ZERO));
        assertTrue(mandelbrot.inSet(Complex.ofCartesian(-1.0, 0)));
    }

    @Test
    public void outsideSet() {
        Mandelbrot mandelbrot = new Mandelbrot(100);

        assertFalse(mandelbrot.inSet(Complex.ofCartesian(1.0, 0)));
    }

    @Test
    public void correctEvaluationParameters() {
        Mandelbrot mandelbrot = new Mandelbrot(100);

        Evaluation evaluation = mandelbrot.evaluate(Complex.ZERO);

        assertEquals(Complex.ZERO, evaluation.getStart());
        assertEquals(100, evaluation.getMaxIteration());
    }
}