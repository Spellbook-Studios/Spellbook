package dk.sebsa.spellbook.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorTest {
    private static final float TEST_COLOR_R = 0.85f;
    private static final float TEST_COLOR_G = 0.335f;
    private static final float TEST_COLOR_B = 1.222f;
    private static final float TEST_COLOR_A = 1f;

    @Test
    void color() {
        var color = Color.color(TEST_COLOR_R, TEST_COLOR_G, TEST_COLOR_B, TEST_COLOR_A);
        assertEquals(color.r, TEST_COLOR_R);
        assertEquals(color.g, TEST_COLOR_G);
        assertEquals(color.b, 1); // Should not be able to go over 1f
        assertEquals(color.a, TEST_COLOR_A);

        var colorMadeWithoutAlpha = Color.color(TEST_COLOR_R, TEST_COLOR_G, TEST_COLOR_B);
        assertEquals(color.a, 1); // Should be one when no color is defined
        assertEquals(color, colorMadeWithoutAlpha); // Colors with same values should be equal
    }
}