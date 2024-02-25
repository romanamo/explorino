package de.romanamo.explorino.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexTest {

    public static double EPSILON = 1e-7;

    @Test
    public void testOfCartesian() {
        Complex cartesian = Complex.ofCartesian(1, 1);

        assertEquals(1, cartesian.getReal(), EPSILON);
        assertEquals(1, cartesian.getImag(), EPSILON);
    }

    @Test
    public void testOfPolar() {
        Complex polar = Complex.ofPolar(Math.PI / 2, 1);

        assertEquals(1, polar.getRadius(), EPSILON);
        assertEquals(Math.PI / 2, polar.getAngle(), EPSILON);
    }

    @Test
    public void testAdditionBasic() {
        Complex a = Complex.ofCartesian(2.0, 3.0);
        Complex b = Complex.ofCartesian(9.0, -5);

        Complex sum = a.add(b);

        assertEquals(11.0, sum.getReal(), EPSILON);
        assertEquals(-2.0, sum.getImag(), EPSILON);
    }

    @Test
    public void testSubtractionBasic() {
        Complex a = Complex.ofCartesian(2.0, 3.0);
        Complex b = Complex.ofCartesian(9.0, -5);

        Complex sum = a.subtract(b);

        assertEquals(-7, sum.getReal(), EPSILON);
        assertEquals(8, sum.getImag(), EPSILON);
    }

    @Test
    public void testMultiplicationBasic() {
        Complex a = Complex.ofCartesian(1,2);
        Complex b = Complex.ofCartesian(3, -4);

        Complex multiplied = a.multiply(b);

        assertEquals(11, multiplied.getReal(), EPSILON);
        assertEquals(2, multiplied.getImag(), EPSILON);
    }

    @Test
    public void testMultiplicationWithScalar() {
        Complex a = Complex.ofCartesian(1.0, -1.0);
        double scalar = 3.0;

        Complex multiplied = a.multiply(scalar);

        assertEquals(3.0, multiplied.getReal(), EPSILON);
        assertEquals(-3.0, multiplied.getImag(), EPSILON);
    }

    @Test
    public void testDivisionException() {
        Complex a = Complex.ofCartesian(-3.0, 4.5);

        assertThrows(ArithmeticException.class, () -> a.divide(Complex.ZERO));
    }

    @Test
    public void testRotationFull() {
        Complex a = Complex.ofCartesian(1, 0);

        Complex rotated = a.rotate(2 * Math.PI);

        assertEquals(1.0, rotated.getReal(), EPSILON);
        assertEquals(0, rotated.getImag(), EPSILON);
    }

    @Test
    public void testConjugation() {
        Complex a = Complex.ofCartesian(0, 1);

        Complex conjugated = a.conjugate();

        assertEquals(0, conjugated.getReal(), EPSILON);
        assertEquals(-1, conjugated.getImag(), EPSILON);
    }

    @Test
    public void testPowToMultiplication() {
        Complex a = Complex.ofCartesian(1.2, 3.5);

        Complex powered = a.multiply(a).multiply(a);
        Complex multiplied = a.pow(3);

        assertEquals(powered.getReal(), multiplied.getReal(), EPSILON);
        assertEquals(powered.getImag(), multiplied.getImag(), EPSILON);
    }

    @Test
    public void testSetAngle() {
        Complex a = Complex.ofCartesian(1, 0);

        a.setAngle(Math.PI / 2);

        assertEquals(0, a.getReal(), EPSILON);
        assertEquals(1, a.getImag(), EPSILON);
    }

    @Test
    public void testSetRadius() {
        Complex a = Complex.ofCartesian(1, 0);

        a.setRadius(2);

        assertEquals(2, a.getReal(), EPSILON);
        assertEquals(0, a.getImag(), EPSILON);
    }

    @Test
    public void testToQuadrant() {
        Complex a = Complex.ofCartesian(1.0, 1.0);

        Complex pp = a.toQuadrant(true, true);
        Complex pn = a.toQuadrant(true, false);
        Complex np = a.toQuadrant(false, true);
        Complex nn = a.toQuadrant(false, false);

        assertTrue(pp.distance(a) < EPSILON);
        assertTrue(pn.distance(Complex.ofCartesian(1.0, -1.0)) < EPSILON);
        assertTrue(np.distance(Complex.ofCartesian(-1.0, 1.0)) < EPSILON);
        assertTrue(nn.distance(Complex.ofCartesian(-1.0, -1.0)) < EPSILON);
    }
}