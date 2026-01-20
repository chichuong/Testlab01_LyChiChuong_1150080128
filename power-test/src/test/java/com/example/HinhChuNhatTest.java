package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HinhChuNhatTest {

    @Test
    void testDienTich() {
        HinhChuNhat r = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        assertEquals(50.0, r.dienTich(), 1e-9);
    }

    @Test
    void testGiaoNhauTrue_overlap() {
        HinhChuNhat a = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        HinhChuNhat b = new HinhChuNhat(new Diem(3, 8), new Diem(8, -2));
        assertTrue(a.giaoNhau(b));
    }

    @Test
    void testGiaoNhauFalse_separated() {
        HinhChuNhat a = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        HinhChuNhat b = new HinhChuNhat(new Diem(6, 10), new Diem(9, 0));
        assertFalse(a.giaoNhau(b));
    }

    @Test
    void testGiaoNhauTouchingEdge_isTrue() {
        // b chạm cạnh phải của a tại x=5
        HinhChuNhat a = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        HinhChuNhat b = new HinhChuNhat(new Diem(5, 8), new Diem(7, -1));
        assertTrue(a.giaoNhau(b));
    }

    @Test
    void testGiaoNhauContained_isTrue() {
        HinhChuNhat a = new HinhChuNhat(new Diem(0, 10), new Diem(10, 0));
        HinhChuNhat b = new HinhChuNhat(new Diem(2, 8), new Diem(5, 2));
        assertTrue(a.giaoNhau(b));
    }

    @Test
    void testConstructorInvalidData_wrongOrder() {
        // topLeft x >= bottomRight x hoặc topLeft y <= bottomRight y => sai
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new HinhChuNhat(new Diem(5, 0), new Diem(0, 10)));
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void testConstructorInvalidData_nullPoint() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new HinhChuNhat(null, new Diem(0, 0)));
        assertEquals("Invalid Data", ex.getMessage());
    }
}
