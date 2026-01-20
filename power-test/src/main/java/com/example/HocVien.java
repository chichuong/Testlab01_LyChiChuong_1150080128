package com.example;

import java.util.Objects;

public class HocVien {
    private final String maSo;
    private final String hoTen;
    private final String queQuan;
    private final double diem1;
    private final double diem2;
    private final double diem3;

    public HocVien(String maSo, String hoTen, String queQuan, double diem1, double diem2, double diem3) {
        if (maSo == null || maSo.isBlank()
                || hoTen == null || hoTen.isBlank()
                || queQuan == null || queQuan.isBlank()) {
            throw new IllegalArgumentException("Invalid Data");
        }
        if (!isValidScore(diem1) || !isValidScore(diem2) || !isValidScore(diem3)) {
            throw new IllegalArgumentException("Invalid Data");
        }

        this.maSo = maSo;
        this.hoTen = hoTen;
        this.queQuan = queQuan;
        this.diem1 = diem1;
        this.diem2 = diem2;
        this.diem3 = diem3;
    }

    private boolean isValidScore(double d) {
        return d >= 0.0 && d <= 10.0;
    }

    public String getMaSo() {
        return maSo;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public double getDiem1() {
        return diem1;
    }

    public double getDiem2() {
        return diem2;
    }

    public double getDiem3() {
        return diem3;
    }

    public double diemTrungBinh() {
        return (diem1 + diem2 + diem3) / 3.0;
    }

    public boolean duDieuKienHocBong() {
        return diemTrungBinh() >= 8.0 && diem1 >= 5.0 && diem2 >= 5.0 && diem3 >= 5.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HocVien))
            return false;
        HocVien hocVien = (HocVien) o;
        return maSo.equals(hocVien.maSo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maSo);
    }
}
