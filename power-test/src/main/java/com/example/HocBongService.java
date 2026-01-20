package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HocBongService {

    public static List<HocVien> danhSachHocBong(List<HocVien> ds) {
        if (ds == null) {
            throw new IllegalArgumentException("Invalid Data");
        }

        List<HocVien> result = new ArrayList<>();
        for (HocVien hv : ds) {
            if (hv != null && hv.duDieuKienHocBong()) {
                result.add(hv);
            }
        }
        return Collections.unmodifiableList(result);
    }
}
