package de.romanamo.explorino.eval;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LyapunovTest {

    @Test
    public void sequenceToIndicesAllUpperCaseTest() {
        int[] sequence = Lyapunov.sequenceToIndices("AABBABA");

        assertArrayEquals(new int[] {0, 0, 1, 1, 0, 1, 0}, sequence);
    }

    @Test
    public void testSequenceToIndicesAllLowerCase() {
        int[] sequence = Lyapunov.sequenceToIndices("aabbaba");

        assertArrayEquals(new int[] {0, 0, 1, 1, 0, 1, 0}, sequence);
    }

    @Test
    public void testSequenceToIndicesMixedCase() {
        int[] sequence = Lyapunov.sequenceToIndices("aAbBAba");

        assertArrayEquals(new int[] {0, 0, 1, 1, 0, 1, 0}, sequence);
    }

    @Test
    public void testSequenceToIndicesCleanUnwantedCharacters() {
        int[] sequence = Lyapunov.sequenceToIndices("a<(Ab$BA&)/b!-*#<a");

        assertArrayEquals(new int[] {0, 0, 1, 1, 0, 1, 0}, sequence);
    }

    @Test
    public void testIndicesToSequence() {
        String sequence = Lyapunov.indicesToSequence(new int[] {0, 0, 1, 1, 0, 1, 0});

        assertEquals("AABBABA", sequence);
    }
}