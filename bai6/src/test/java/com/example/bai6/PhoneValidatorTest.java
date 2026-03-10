package com.example.bai6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PhoneValidatorTest {

    // ============ BASIS PATH TEST CASES (từ CFG, CC = 6) ============

    @Test
    @DisplayName("TC1 - Path 1: null input → false")
    void testNull() {
        assertFalse(PhoneValidator.isValid(null));
    }

    @Test
    @DisplayName("TC2 - Path 1: empty string → false")
    void testEmpty() {
        assertFalse(PhoneValidator.isValid(""));
    }

    @Test
    @DisplayName("TC3 - Path 2: chứa ký tự không hợp lệ → false")
    void testInvalidChars() {
        assertFalse(PhoneValidator.isValid("09abcdefgh"));
    }

    @Test
    @DisplayName("TC4 - Path 3: không bắt đầu bằng 0 hoặc +84 → false")
    void testNotStartWith0Or84() {
        assertFalse(PhoneValidator.isValid("1234567890"));
    }

    @Test
    @DisplayName("TC5 - Path 4: đúng prefix nhưng sai độ dài (9 số) → false")
    void testWrongLengthShort() {
        assertFalse(PhoneValidator.isValid("091234567"));
    }

    @Test
    @DisplayName("TC6 - Path 5: đúng 10 số nhưng prefix không hợp lệ (01x) → false")
    void testInvalidPrefix01() {
        assertFalse(PhoneValidator.isValid("0123456789"));
    }

    @Test
    @DisplayName("TC7 - Path 6: số hợp lệ 09x → true")
    void testValid09x() {
        assertTrue(PhoneValidator.isValid("0912345678"));
    }

    // ============ TEST CASES BỔ SUNG (Boundary + +84 path) ============

    @Test
    @DisplayName("TC8 - Path qua N5: +84 hợp lệ → true")
    void testValidWith84Prefix() {
        assertTrue(PhoneValidator.isValid("+84912345678"));
    }

    @Test
    @DisplayName("TC9 - Có khoảng trắng → true (sau chuẩn hóa)")
    void testValidWithSpaces() {
        assertTrue(PhoneValidator.isValid("091 234 5678"));
    }

    @Test
    @DisplayName("TC10 - +84 có khoảng trắng → true")
    void testValid84WithSpaces() {
        assertTrue(PhoneValidator.isValid("+84 912345678"));
    }

    @Test
    @DisplayName("TC11 - Prefix 03x hợp lệ → true")
    void testValid03x() {
        assertTrue(PhoneValidator.isValid("0312345678"));
    }

    @Test
    @DisplayName("TC12 - Prefix 05x hợp lệ → true")
    void testValid05x() {
        assertTrue(PhoneValidator.isValid("0512345678"));
    }

    @Test
    @DisplayName("TC13 - Prefix 07x hợp lệ → true")
    void testValid07x() {
        assertTrue(PhoneValidator.isValid("0712345678"));
    }

    @Test
    @DisplayName("TC14 - Prefix 08x hợp lệ → true")
    void testValid08x() {
        assertTrue(PhoneValidator.isValid("0812345678"));
    }

    @Test
    @DisplayName("TC15 - Quá dài (11 số) → false")
    void testTooLong() {
        assertFalse(PhoneValidator.isValid("09123456789"));
    }

    @Test
    @DisplayName("TC16 - Prefix 04x không hợp lệ → false")
    void testInvalidPrefix04() {
        assertFalse(PhoneValidator.isValid("0412345678"));
    }

    @Test
    @DisplayName("TC17 - Prefix 06x không hợp lệ → false")
    void testInvalidPrefix06() {
        assertFalse(PhoneValidator.isValid("0612345678"));
    }

    @Test
    @DisplayName("TC18 - +84 nhưng prefix không hợp lệ → false")
    void testInvalid84WithBadPrefix() {
        assertFalse(PhoneValidator.isValid("+84112345678"));
    }

    @Test
    @DisplayName("TC19 - Chỉ khoảng trắng → false")
    void testOnlySpaces() {
        assertFalse(PhoneValidator.isValid("   "));
    }

    @Test
    @DisplayName("TC20 - Chứa ký tự đặc biệt khác → false")
    void testSpecialChars() {
        assertFalse(PhoneValidator.isValid("091-234-5678"));
    }
}
