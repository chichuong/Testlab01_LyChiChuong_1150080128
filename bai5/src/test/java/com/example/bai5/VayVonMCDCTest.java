package com.example.bai5;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class MC/DC cho hàm duDieuKienVay.
 *
 * Các điều kiện đơn:
 * A: tuoi >= 22
 * B: thuNhap >= 10_000_000
 * C: coTaiSanBaoLanh
 * D: dienTinDung >= 700
 *
 * Biểu thức: (A && B) && (C || D)
 *
 * Bảng MC/DC tối thiểu (5 test case):
 * -----------------------------------------------
 * | TC | A | B | C | D | Kết quả | Cặp MC/DC |
 * |------|---|---|---|---|---------|------------|
 * | TC2 | T | T | T | F | true | A(2,10) B(2,6) C(2,4) |
 * | TC3 | T | T | F | T | true | D(3,4) |
 * | TC4 | T | T | F | F | false | C(2,4) D(3,4) |
 * | TC6 | T | F | T | F | false | B(2,6) |
 * | TC10 | F | T | T | F | false | A(2,10) |
 * -----------------------------------------------
 */
public class VayVonMCDCTest {

    // ==================== Test riêng cho từng cặp MC/DC ====================

    // --- Cặp MC/DC cho A (tuoi >= 22): TC2 vs TC10 ---
    @Test(description = "MC/DC A - tuoi >= 22 (A=T): tuoi=25, thuNhap=15tr, coTaiSan=true, dienTinDung=600 -> TRUE")
    public void testMCDC_TuoiDocLap_LonHonHoac22() {
        boolean result = VayVon.duDieuKienVay(25, 15_000_000, true, 600);
        Assert.assertTrue(result, "Tuoi=25 (>=22), thuNhap=15tr, coTaiSan=true => phải được vay");
    }

    @Test(description = "MC/DC A - tuoi < 22 (A=F): tuoi=20, thuNhap=15tr, coTaiSan=true, dienTinDung=600 -> FALSE")
    public void testMCDC_TuoiDocLap_ThapHon22() {
        boolean result = VayVon.duDieuKienVay(20, 15_000_000, true, 600);
        Assert.assertFalse(result, "Tuoi=20 (<22) => không được vay dù các ĐK khác thỏa");
    }

    // --- Cặp MC/DC cho B (thuNhap >= 10tr): TC2 vs TC6 ---
    @Test(description = "MC/DC B - thuNhap >= 10tr (B=T): tuoi=25, thuNhap=15tr, coTaiSan=true, dienTinDung=600 -> TRUE")
    public void testMCDC_ThuNhapDocLap_DuThuNhap() {
        boolean result = VayVon.duDieuKienVay(25, 15_000_000, true, 600);
        Assert.assertTrue(result, "ThuNhap=15tr (>=10tr), tuoi=25, coTaiSan=true => phải được vay");
    }

    @Test(description = "MC/DC B - thuNhap < 10tr (B=F): tuoi=25, thuNhap=5tr, coTaiSan=true, dienTinDung=600 -> FALSE")
    public void testMCDC_ThuNhapDocLap_KhongDuThuNhap() {
        boolean result = VayVon.duDieuKienVay(25, 5_000_000, true, 600);
        Assert.assertFalse(result, "ThuNhap=5tr (<10tr) => không được vay dù các ĐK khác thỏa");
    }

    // --- Cặp MC/DC cho C (coTaiSanBaoLanh): TC2 vs TC4 ---
    @Test(description = "MC/DC C - coTaiSan=true (C=T): tuoi=25, thuNhap=15tr, coTaiSan=true, dienTinDung=600 -> TRUE")
    public void testMCDC_TaiSanDocLap_CoTaiSan() {
        boolean result = VayVon.duDieuKienVay(25, 15_000_000, true, 600);
        Assert.assertTrue(result, "CoTaiSan=true, dienTinDung=600 (<700) => được vay nhờ tài sản");
    }

    @Test(description = "MC/DC C - coTaiSan=false (C=F): tuoi=25, thuNhap=15tr, coTaiSan=false, dienTinDung=600 -> FALSE")
    public void testMCDC_TaiSanDocLap_KhongCoTaiSan() {
        boolean result = VayVon.duDieuKienVay(25, 15_000_000, false, 600);
        Assert.assertFalse(result, "CoTaiSan=false, dienTinDung=600 (<700) => không đủ ĐK bảo đảm");
    }

    // --- Cặp MC/DC cho D (dienTinDung >= 700): TC3 vs TC4 ---
    @Test(description = "MC/DC D - dienTinDung >= 700 (D=T): tuoi=25, thuNhap=15tr, coTaiSan=false, dienTinDung=750 -> TRUE")
    public void testMCDC_TinDungDocLap_TinDungTot() {
        boolean result = VayVon.duDieuKienVay(25, 15_000_000, false, 750);
        Assert.assertTrue(result, "DienTinDung=750 (>=700), coTaiSan=false => được vay nhờ tín dụng tốt");
    }

    @Test(description = "MC/DC D - dienTinDung < 700 (D=F): tuoi=25, thuNhap=15tr, coTaiSan=false, dienTinDung=600 -> FALSE")
    public void testMCDC_TinDungDocLap_TinDungThap() {
        boolean result = VayVon.duDieuKienVay(25, 15_000_000, false, 600);
        Assert.assertFalse(result, "DienTinDung=600 (<700), coTaiSan=false => không đủ ĐK bảo đảm");
    }

    // ==================== DataProvider gộp tất cả MC/DC test case
    // ====================

    @DataProvider(name = "mcdcData")
    public Object[][] mcdcDataProvider() {
        return new Object[][] {
                // { tuoi, thuNhap, coTaiSan, dienTinDung, expectedResult, moTa }
                { 25, 15_000_000.0, true, 600, true, "TC2: A=T,B=T,C=T,D=F -> TRUE  (dùng cho cặp A,B,C)" },
                { 25, 15_000_000.0, false, 750, true, "TC3: A=T,B=T,C=F,D=T -> TRUE  (dùng cho cặp D)" },
                { 25, 15_000_000.0, false, 600, false, "TC4: A=T,B=T,C=F,D=F -> FALSE (dùng cho cặp C,D)" },
                { 25, 5_000_000.0, true, 600, false, "TC6: A=T,B=F,C=T,D=F -> FALSE (dùng cho cặp B)" },
                { 20, 15_000_000.0, true, 600, false, "TC10: A=F,B=T,C=T,D=F -> FALSE (dùng cho cặp A)" },
        };
    }

    @Test(dataProvider = "mcdcData", description = "Test MC/DC tổng hợp qua DataProvider - 5 test case tối thiểu cho 4 điều kiện đơn")
    public void testMCDC_DataProvider(int tuoi, double thuNhap, boolean coTaiSan,
            int dienTinDung, boolean expected, String moTa) {
        boolean actual = VayVon.duDieuKienVay(tuoi, thuNhap, coTaiSan, dienTinDung);
        Assert.assertEquals(actual, expected, "FAIL [" + moTa + "]");
    }
}
