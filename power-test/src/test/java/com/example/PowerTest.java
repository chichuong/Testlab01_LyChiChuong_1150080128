package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PowerTest {

    @Test
    void testExponentZero() {
        assertEquals(1.0, Power.power(5.0, 0), 0.000001);
    }

    @Test
    void testPositiveExponent() {
        assertEquals(8.0, Power.power(2.0, 3), 0.000001);
    }

    @Test
    void testNegativeExponent() {
        assertEquals(0.25, Power.power(2.0, -2), 0.000001);
    }

    @Test
    void testNegativeBase() {
        assertEquals(-8.0, Power.power(-2.0, 3), 0.000001);
    }
}
