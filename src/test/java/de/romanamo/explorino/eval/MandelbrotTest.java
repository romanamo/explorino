package de.romanamo.explorino.eval;

import de.romanamo.explorino.math.Complex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MandelbrotTest {

    @Test
    public void testInsideSet() {
        Mandelbrot mandelbrot = new Mandelbrot(100);

        assertTrue(mandelbrot.inSet(Complex.ZERO));
        assertTrue(mandelbrot.inSet(Complex.ofCartesian(-1.0, 0)));
    }

    @Test
    public void testOutsideSet() {
        Mandelbrot mandelbrot = new Mandelbrot(100);

        assertFalse(mandelbrot.inSet(Complex.ofCartesian(1.0, 0)));
    }
}