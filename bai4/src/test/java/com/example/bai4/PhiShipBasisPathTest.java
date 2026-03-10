package com.example.bai4;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PhiShipBasisPathTest {

    @Test(description = "Path 1: Trong luong khong hop le (trongLuong <= 0) -> IllegalArgumentException")
    public void testPath1_InvalidWeight() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> PhiShip.tinhPhiShip(-1, "noi_thanh", false));
    }

    @Test(description = "Path 2: Noi thanh, trong luong <= 5kg, khong member -> phi = 15000")
    public void testPath2_NoiThanhNheKhongMember() {
        double expected = 15000;
        Assert.assertEquals(PhiShip.tinhPhiShip(3, "noi_thanh", false), expected, 0.01);
    }

    @Test(description = "Path 3: Noi thanh, trong luong > 5kg, khong member -> phi = 15000 + (7-5)*2000 = 19000")
    public void testPath3_NoiThanhNangKhongMember() {
        double expected = 19000;
        Assert.assertEquals(PhiShip.tinhPhiShip(7, "noi_thanh", false), expected, 0.01);
    }

    @Test(description = "Path 4: Ngoai thanh, trong luong <= 3kg, khong member -> phi = 25000")
    public void testPath4_NgoaiThanhNheKhongMember() {
        double expected = 25000;
        Assert.assertEquals(PhiShip.tinhPhiShip(2, "ngoai_thanh", false), expected, 0.01);
    }

    @Test(description = "Path 5: Ngoai thanh, trong luong > 3kg, khong member -> phi = 25000 + (5-3)*3000 = 31000")
    public void testPath5_NgoaiThanhNangKhongMember() {
        double expected = 31000;
        Assert.assertEquals(PhiShip.tinhPhiShip(5, "ngoai_thanh", false), expected, 0.01);
    }

    @Test(description = "Path 6: Tinh khac, trong luong <= 2kg, khong member -> phi = 50000")
    public void testPath6_TinhKhacNheKhongMember() {
        double expected = 50000;
        Assert.assertEquals(PhiShip.tinhPhiShip(1, "tinh_khac", false), expected, 0.01);
    }

    @Test(description = "Path 7: Tinh khac, trong luong > 2kg, khong member -> phi = 50000 + (4-2)*5000 = 60000")
    public void testPath7_TinhKhacNangKhongMember() {
        double expected = 60000;
        Assert.assertEquals(PhiShip.tinhPhiShip(4, "tinh_khac", false), expected, 0.01);
    }

    @Test(description = "Path 8: Noi thanh, trong luong <= 5kg, la member -> phi = 15000 * 0.9 = 13500")
    public void testPath8_NoiThanhNheLaMember() {
        double expected = 13500;
        Assert.assertEquals(PhiShip.tinhPhiShip(3, "noi_thanh", true), expected, 0.01);
    }
}
