package dk.sebsa.spellbook.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathfTest {
    @Test
    void wrap() {
        assertEquals(0, Mathf.wrap(0, 0, 100));
        assertEquals(50, Mathf.wrap(50, 0, 100));
        assertEquals(0, Mathf.wrap(100, 0, 100));
        assertEquals(10, Mathf.wrap(110, 0, 100));
        assertEquals(90, Mathf.wrap(-10, 0, 100));
    }

    @Test
    void clamp() {
        assertEquals(0, Mathf.clamp(0, 0, 100));
        assertEquals(0, Mathf.clamp(-20, 0, 100));
        assertEquals(0, Mathf.clamp(-120, 0, 100));
        assertEquals(12, Mathf.clamp(12, 0, 100));
        assertEquals(100, Mathf.clamp(100, 0, 100));
        assertEquals(100, Mathf.clamp(101, 0, 100));
    }
}