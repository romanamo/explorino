package de.romanamo.explorino.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericTest {

    @Test
    public void testCeilDivDividendBigger() {
        assertEquals(2, Numeric.ceilDiv(13, 7));
    }

    @Test
    public void testCeilDivDividendSmaller() {
        assertEquals(1, Numeric.ceilDiv(1, 2));
    }

    @Test
    public void testCeilDivEqual() {
        assertEquals(1, Numeric.ceilDiv(1, 1));
    }

    @Test
    public void testCeilDivZeroDivision() {
        assertThrows(ArithmeticException.class, () -> Numeric.ceilDiv(1, 0));
    }

}