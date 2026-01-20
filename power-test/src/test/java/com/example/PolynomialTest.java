package com.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PolynomialTest {

    @Test
    void testCalBasic() {
        // P(x) = 1 + 2x + 3x^2, n=2, a=[1,2,3]
        Polynomial p = new Polynomial(2, List.of(1, 2, 3));
        assertEquals(17, p.cal(2)); // 1 + 4 + 12 = 17
    }

    @Test
    void testCalWithNegativeX() {
        // P(x) = 1 - 2x + 3x^2, x=-2
        Polynomial p = new Polynomial(2, List.of(1, -2, 3));
        assertEquals(17, p.cal(-2)); // 1 + 4 + 12 = 17
    }

    @Test
    void testCalZeroDegree() {
        // P(x) = 5, n=0, a=[5]
        Polynomial p = new Polynomial(0, List.of(5));
        assertEquals(5, p.cal(999));
    }

    @Test
    void testConstructorInvalidCoefficientCountLess() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Polynomial(2, List.of(1, 2)) // thiếu a2
        );
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void testConstructorInvalidCoefficientCountMore() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Polynomial(1, List.of(1, 2, 3)) // thừa hệ số
        );
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void testConstructorInvalidNegativeN() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Polynomial(-1, List.of(1)));
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void testConstructorNullList() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Polynomial(0, null));
        assertEquals("Invalid Data", ex.getMessage());
    }
}
