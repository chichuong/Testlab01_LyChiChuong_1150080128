package com.example.bai3;

public class XepLoai {

    public static String xepLoai(int diemTB, boolean coThiLai) {
        if (diemTB < 0 || diemTB > 10) { // Điều kiện 1
            return "Diem khong hop le";
        }
        if (diemTB >= 8.5) { // Điều kiện 2
            return "Gioi";
        } else if (diemTB >= 7.0) { // Điều kiện 3
            return "Kha";
        } else if (diemTB >= 5.5) { // Điều kiện 4
            return "Trung Binh";
        } else {
            if (coThiLai) { // Điều kiện 5
                return "Thi lai";
            }
            return "Yeu - Hoc lai";
        }
    }
}
