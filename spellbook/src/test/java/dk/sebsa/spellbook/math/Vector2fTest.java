package dk.sebsa.spellbook.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Vector2fTest {

    @Test
    void zero() {
        var vector = new Vector2f(1234, 1234);
        vector.zero();
        assertEquals(vector.x, 0);
        assertEquals(vector.y, 0);
    }

    @Test
    void add() {
        var vector = new Vector2f(1234, 1234);
        vector = vector.add(1111);
        assertEquals(vector.x, 2345);
        assertEquals(vector.y, 2345);
    }

    @Test
    void testAdd() {
        var vector = new Vector2f(1234, 1234);
        vector = vector.add(1111, 1110);
        assertEquals(vector.x, 2345);
        assertEquals(vector.y, 2344);
    }

    @Test
    void testAdd1() {
        var vector = new Vector2f(1234, 1234);
        vector = vector.add(new Vector2f(1, 1));
        assertEquals(vector.x, 1235);
        assertEquals(vector.y, 1235);
    }

    @Test
    void sub() {
        var vector = new Vector2f(1234, 1234);
        vector = vector.sub(1111);
        assertEquals(vector.x, 123);
        assertEquals(vector.y, 123);
    }

    @Test
    void testSub() {
        var vector = new Vector2f(1234, 1234);
        vector = vector.sub(1111, 1110);
        assertEquals(vector.x, 123);
        assertEquals(vector.y, 124);
    }

    @Test
    void testSub1() {
        var vector = new Vector2f(1234, 1234);
        vector = vector.sub(new Vector2f(1, 1));
        assertEquals(vector.x, 1233);
        assertEquals(vector.y, 1233);
    }

    @Test
    void div() {
        var vector = new Vector2f(128, 64);
        vector = vector.div(4);
        assertEquals(vector.x, 32);
        assertEquals(vector.y, 16);
    }

    @Test
    void testDiv() {
        var vector = new Vector2f(128, 64);
        vector = vector.div(4, 2);
        assertEquals(vector.x, 32);
        assertEquals(vector.y, 32);
    }

    @Test
    void testDiv1() {
        var vector = new Vector2f(128, 64);
        vector = vector.div(new Vector2f(2, 4));
        assertEquals(vector.x, 64);
        assertEquals(vector.y, 16);
    }

    @Test
    void mul() {
        var vector = new Vector2f(4, 16);
        vector = vector.mul(4);
        assertEquals(vector.x, 16);
        assertEquals(vector.y, 64);
    }

    @Test
    void testMul() {
        var vector = new Vector2f(4, 16);
        vector = vector.mul(8, 2);
        assertEquals(vector.x, 32);
        assertEquals(vector.y, 32);
    }

    @Test
    void testMul1() {
        var vector = new Vector2f(4, 16);
        vector = vector.mul(new Vector2f(4, 8));
        assertEquals(vector.x, 16);
        assertEquals(vector.y, 128);
    }

    @Test
    void min() {
        var a = new Vector2f(5, 100);
        assertEquals(5, a.min());
    }

    @Test
    void max() {
        var a = new Vector2f(5, 100);
        assertEquals(100, a.max());
    }
}