package com.example.bai5;

public class VayVon {

    public static boolean duDieuKienVay(int tuoi, double thuNhap,
            boolean coTaiSanBaoLanh, int dienTinDung) {
        // Điều kiện chính: phải là người trưởng thành có thu nhập ổn định
        boolean dieuKienCoBan = (tuoi >= 22) && (thuNhap >= 10_000_000);
        // Điều kiện bảo đảm: có tài sản hoặc tín dụng tốt
        boolean dieuKienBaoDam = coTaiSanBaoLanh || (dienTinDung >= 700);
        // Được vay khi cả 2 nhóm điều kiện đều thỏa mãn
        return dieuKienCoBan && dieuKienBaoDam;
    }
}
