package com.example.bai3;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XepLoaiTest {

    @DataProvider(name = "branchCoverageData")
    public Object[][] branchCoverageData() {
        return new Object[][] {
                // TC1: C1-True (diemTB < 0)
                { -1, false, "Diem khong hop le" },
                // TC2: C1-False, C2-True (diemTB >= 8.5 → int >= 9)
                { 9, false, "Gioi" },
                // TC3: C2-False, C3-True (diemTB >= 7.0)
                { 7, false, "Kha" },
                // TC4: C3-False, C4-True (diemTB >= 5.5 → int >= 6)
                { 6, false, "Trung Binh" },
                // TC5: C4-False, C5-True (coThiLai = true)
                { 4, true, "Thi lai" },
                // TC6: C5-False (coThiLai = false)
                { 4, false, "Yeu - Hoc lai" }
        };
    }

    // TC1: diemTB = -1 → "Diem khong hop le" — Phủ nhánh C1-True
    @Test
    public void testDiemKhongHopLe() {
        Assert.assertEquals(XepLoai.xepLoai(-1, false), "Diem khong hop le");
    }

    // TC2: diemTB = 9 → "Gioi" — Phủ nhánh C1-False, C2-True
    @Test
    public void testGioi() {
        Assert.assertEquals(XepLoai.xepLoai(9, false), "Gioi");
    }

    // TC3: diemTB = 7 → "Kha" — Phủ nhánh C2-False, C3-True
    @Test
    public void testKha() {
        Assert.assertEquals(XepLoai.xepLoai(7, false), "Kha");
    }

    // TC4: diemTB = 6 → "Trung Binh" — Phủ nhánh C3-False, C4-True
    @Test
    public void testTrungBinh() {
        Assert.assertEquals(XepLoai.xepLoai(6, false), "Trung Binh");
    }

    // TC5: diemTB = 4, coThiLai = true → "Thi lai" — Phủ nhánh C4-False, C5-True
    @Test
    public void testThiLai() {
        Assert.assertEquals(XepLoai.xepLoai(4, true), "Thi lai");
    }

    // TC6: diemTB = 4, coThiLai = false → "Yeu - Hoc lai" — Phủ nhánh C5-False
    @Test
    public void testYeuHocLai() {
        Assert.assertEquals(XepLoai.xepLoai(4, false), "Yeu - Hoc lai");
    }

    // Test sử dụng DataProvider để chạy tất cả test case Branch Coverage
    @Test(dataProvider = "branchCoverageData")
    public void testBranchCoverage(int diemTB, boolean coThiLai, String expected) {
        Assert.assertEquals(XepLoai.xepLoai(diemTB, coThiLai), expected);
    }
}
