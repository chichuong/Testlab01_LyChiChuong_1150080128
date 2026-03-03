package com.example.demo.model;

import jakarta.validation.constraints.*;

public class RegistrationForm {

    @NotBlank(message = "Họ và tên không được để trống")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]{2,50}$", message = "Họ và tên chỉ chứa chữ cái và dấu cách, độ dài 2–50 ký tự")
    private String hoVaTen;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Pattern(regexp = "^[a-z][a-z0-9_]{4,19}$", message = "Tên đăng nhập: chữ thường, số, dấu gạch dưới, bắt đầu bằng chữ cái, 5–20 ký tự")
    private String tenDangNhap;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại Việt Nam: bắt đầu bằng 0, gồm 10 chữ số")
    private String soDienThoai;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 32, message = "Mật khẩu phải từ 8–32 ký tự")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,32}$", message = "Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số, 1 ký tự đặc biệt")
    private String matKhau;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String xacNhanMatKhau;

    @Pattern(regexp = "^(\\d{2}/\\d{2}/\\d{4})?$", message = "Ngày sinh phải có định dạng dd/mm/yyyy")
    private String ngaySinh;

    private String gioiTinh;

    @Pattern(regexp = "^([A-Z0-9]{8})?$", message = "Mã giới thiệu gồm 8 ký tự chữ hoa và số")
    private String maGioiThieu;

    @AssertTrue(message = "Bạn phải đồng ý với Điều khoản sử dụng")
    private boolean dongYDieuKhoan;

    // Getters and Setters

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getXacNhanMatKhau() {
        return xacNhanMatKhau;
    }

    public void setXacNhanMatKhau(String xacNhanMatKhau) {
        this.xacNhanMatKhau = xacNhanMatKhau;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMaGioiThieu() {
        return maGioiThieu;
    }

    public void setMaGioiThieu(String maGioiThieu) {
        this.maGioiThieu = maGioiThieu;
    }

    public boolean isDongYDieuKhoan() {
        return dongYDieuKhoan;
    }

    public void setDongYDieuKhoan(boolean dongYDieuKhoan) {
        this.dongYDieuKhoan = dongYDieuKhoan;
    }
}
