package de.romanamo.explorino.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolynomTest {

    @Test
    public void testDerivateOneCoefficients() {
        Polynom polynom = new Polynom(1.0);

        Polynom derivative = polynom.derivate();

        assertEquals(new Polynom(), derivative);
    }

    @Test
    public void testDerivateZeroCoefficients() {
        Polynom polynom = new Polynom();

        Polynom derivative = polynom.derivate();

        assertEquals(polynom, derivative);
    }

    @Test
    public void testToString() {
        Polynom polynom = new Polynom(1, 2, 3);

        assertEquals("1.0^0 + 2.0^1 + 3.0^2", polynom.toString());
    }
}