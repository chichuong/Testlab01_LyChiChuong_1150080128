package com.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HocBongServiceTest {

    @Test
    void testHocVienDuDieuKienHocBong_true() {
        HocVien hv = new HocVien("SV01", "An", "HN", 8.0, 8.0, 8.0);
        assertTrue(hv.duDieuKienHocBong());
    }

    @Test
    void testHocVienTBBang8_vaKhongMonNaoDuoi5_true() {
        // TB = (9 + 7 + 8)/3 = 8.0, không môn nào < 5
        HocVien hv = new HocVien("SV02", "Binh", "DN", 9.0, 7.0, 8.0);
        assertEquals(8.0, hv.diemTrungBinh(), 1e-9);
        assertTrue(hv.duDieuKienHocBong());
    }

    @Test
    void testHocVienTBTren8_nhungCoMonDuoi5_false() {
        // TB = (10 + 10 + 4)/3 = 8.0 nhưng có môn 4 < 5 => không đạt
        HocVien hv = new HocVien("SV03", "Chi", "HCM", 10.0, 10.0, 4.0);
        assertFalse(hv.duDieuKienHocBong());
    }

    @Test
    void testHocVienTBDuoi8_false() {
        HocVien hv = new HocVien("SV04", "Dung", "HP", 7.5, 8.0, 8.0); // TB < 8
        assertFalse(hv.duDieuKienHocBong());
    }

    @Test
    void testDanhSachHocBong_locDung() {
        HocVien hv1 = new HocVien("SV01", "An", "HN", 8.0, 8.0, 8.0); // đạt
        HocVien hv2 = new HocVien("SV02", "Binh", "DN", 9.0, 7.0, 8.0); // đạt
        HocVien hv3 = new HocVien("SV03", "Chi", "HCM", 10.0, 10.0, 4.0); // không
        HocVien hv4 = new HocVien("SV04", "Dung", "HP", 7.5, 8.0, 8.0); // không

        List<HocVien> result = HocBongService.danhSachHocBong(List.of(hv1, hv2, hv3, hv4));
        assertEquals(2, result.size());
        assertTrue(result.contains(hv1));
        assertTrue(result.contains(hv2));
    }

    @Test
    void testDanhSachHocBong_boQuaNullHocVien() {
        HocVien hv1 = new HocVien("SV01", "An", "HN", 8.0, 8.0, 8.0);

        // List.of(...) KHÔNG cho phép null, nên phải dùng Arrays.asList(...)
        List<HocVien> input = Arrays.asList(null, hv1);

        List<HocVien> result = HocBongService.danhSachHocBong(input);
        assertEquals(1, result.size());
        assertTrue(result.contains(hv1));
    }

    @Test
    void testDanhSachHocBong_nullList_throw() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> HocBongService.danhSachHocBong(null));
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void testHocVienInvalidScore_throw() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new HocVien("SV99", "E", "QN", 11.0, 8.0, 7.0));
        assertEquals("Invalid Data", ex.getMessage());
    }
}
