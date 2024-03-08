package de.romanamo.explorino.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    @Test
    public void testDerivateOneCoefficients() {
        Polynomial polynomial = new Polynomial(1.0);

        Polynomial derivative = polynomial.derivate();

        assertEquals(new Polynomial(), derivative);
    }

    @Test
    public void testDerivateZeroCoefficients() {
        Polynomial polynomial = new Polynomial();

        Polynomial derivative = polynomial.derivate();

        assertEquals(polynomial, derivative);
    }

    @Test
    public void testToString() {
        Polynomial polynomial = new Polynomial(1, 2, 3);

        assertEquals("1.0^0 + 2.0^1 + 3.0^2", polynomial.toString());
    }
}