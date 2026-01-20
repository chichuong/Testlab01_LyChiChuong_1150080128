package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RadixTest {

    @Test
    void testBinaryConversion() {
        Radix r = new Radix(10);
        assertEquals("1010", r.convertDecimalToAnother(2));
    }

    @Test
    void testOctalConversion() {
        Radix r = new Radix(83);
        assertEquals("123", r.convertDecimalToAnother(8));
    }

    @Test
    void testHexConversion() {
        Radix r = new Radix(255);
        assertEquals("FF", r.convertDecimalToAnother(16));
    }

    @Test
    void testBase10Conversion() {
        Radix r = new Radix(1234);
        assertEquals("1234", r.convertDecimalToAnother(10));
    }

    @Test
    void testZero() {
        Radix r = new Radix(0);
        assertEquals("0", r.convertDecimalToAnother(2));
    }

    @Test
    void testInvalidRadixLow() {
        Radix r = new Radix(10);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> r.convertDecimalToAnother(1));
        assertEquals("Invalid Radix", ex.getMessage());
    }

    @Test
    void testInvalidRadixHigh() {
        Radix r = new Radix(10);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> r.convertDecimalToAnother(17));
        assertEquals("Invalid Radix", ex.getMessage());
    }

    @Test
    void testNegativeNumberConstructor() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Radix(-5));
        assertEquals("Incorrect Value", ex.getMessage());
    }
}
