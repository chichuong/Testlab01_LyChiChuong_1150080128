package com.example.bai3;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TinhTienNuocTest {

    // TC1: soM3 = 0 → return 0 — Phủ nhánh N1-True
    @Test
    public void testSoM3BangKhong() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(0, "dan_cu"), 0.0);
    }

    // TC2: soM3 = 5, "ho_ngheo" → 5 * 5000 = 25000 — Phủ nhánh N1-False, N2-True
    @Test
    public void testHoNgheo() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(5, "ho_ngheo"), 25000.0);
    }

    // TC3: soM3 = 5, "dan_cu" → 5 * 7500 = 37500 — Phủ nhánh N2-False, N3-True,
    // N4-True
    @Test
    public void testDanCuDuoi10() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(5, "dan_cu"), 37500.0);
    }

    // TC4: soM3 = 15, "dan_cu" → 15 * 9900 = 148500 — Phủ nhánh N4-False, N5-True
    @Test
    public void testDanCu11Den20() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(15, "dan_cu"), 148500.0);
    }

    // TC5: soM3 = 25, "dan_cu" → 25 * 11400 = 285000 — Phủ nhánh N5-False
    @Test
    public void testDanCuTren20() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(25, "dan_cu"), 285000.0);
    }

    // TC6: soM3 = 10, "kinh_doanh" → 10 * 22000 = 220000 — Phủ nhánh N3-False
    @Test
    public void testKinhDoanh() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(10, "kinh_doanh"), 220000.0);
    }
}
