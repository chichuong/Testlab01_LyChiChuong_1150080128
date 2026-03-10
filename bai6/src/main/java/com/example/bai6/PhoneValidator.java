package com.example.bai6;

public class PhoneValidator {

    /**
     * Kiểm tra số điện thoại Việt Nam hợp lệ.
     *
     * Quy tắc:
     * - Bắt đầu bằng 0 hoặc +84
     * - Sau chuẩn hóa: prefix hợp lệ là 03x, 05x, 07x, 08x, 09x
     * - Tổng 10 chữ số sau chuẩn hóa (0xxxxxxxxx)
     * - Chỉ chứa 0-9, +, khoảng trắng (trước chuẩn hóa)
     */
    public static boolean isValid(String phone) {
        // N1: Kiểm tra null hoặc rỗng
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        // N2: Kiểm tra ký tự hợp lệ (chỉ chứa 0-9, +, khoảng trắng)
        if (!phone.matches("[0-9+\\s]+")) {
            return false;
        }

        // N3: Xóa khoảng trắng
        phone = phone.replaceAll("\\s", "");

        // N4-N5: Chuẩn hóa +84 → 0
        if (phone.startsWith("+84")) {
            phone = "0" + phone.substring(3);
        }

        // N6: Phải bắt đầu bằng 0
        if (!phone.startsWith("0")) {
            return false;
        }

        // N7: Phải đúng 10 chữ số
        if (phone.length() != 10) {
            return false;
        }

        // N8: Kiểm tra prefix hợp lệ (03, 05, 07, 08, 09)
        char secondDigit = phone.charAt(1);
        if (secondDigit == '3' || secondDigit == '5' || secondDigit == '7'
                || secondDigit == '8' || secondDigit == '9') {
            return true; // N9
        }

        return false; // N10
    }
}
