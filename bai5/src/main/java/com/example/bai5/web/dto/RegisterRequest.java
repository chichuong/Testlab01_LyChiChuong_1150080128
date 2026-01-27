package com.example.bai5.web.dto;

import com.example.bai5.domain.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class RegisterRequest {

    // 1) Mã KH: required, 6-10, chỉ chữ+số
    @NotBlank(message = "Mã khách hàng là bắt buộc.")
    @Size(min = 6, max = 10, message = "Mã khách hàng phải từ 6 đến 10 ký tự.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Mã khách hàng chỉ được chứa chữ cái và số.")
    private String customerCode;

    // 2) Họ tên: required, 5-50, tiếng Việt có dấu + khoảng trắng
    @NotBlank(message = "Họ và tên là bắt buộc.")
    @Size(min = 5, max = 50, message = "Họ và tên phải từ 5 đến 50 ký tự.")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Họ và tên chỉ được chứa chữ cái (kể cả có dấu) và khoảng trắng.")
    private String fullName;

    // 3) Email: required, đúng định dạng
    @NotBlank(message = "Email là bắt buộc.")
    @Email(message = "Email không đúng định dạng (vd: nguyenvana@email.com).")
    private String email;

    // 4) SĐT: required, chỉ số, 10-12, bắt đầu 0
    @NotBlank(message = "Số điện thoại là bắt buộc.")
    @Pattern(regexp = "^0\\d{9,11}$", message = "Số điện thoại phải bắt đầu bằng 0 và có độ dài 10-12 số.")
    private String phone;

    // 5) Địa chỉ: required, max 255
    @NotBlank(message = "Địa chỉ là bắt buộc.")
    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự.")
    private String address;

    // 6) Mật khẩu: required, min 8
    @NotBlank(message = "Mật khẩu là bắt buộc.")
    @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự.")
    private String password;

    // 7) Confirm: required
    @NotBlank(message = "Xác nhận mật khẩu là bắt buộc.")
    private String confirmPassword;

    // 8) Ngày sinh optional (check 18 tuổi ở service)
    private LocalDate dob;

    // 9) Giới tính optional
    private Gender gender;

    // 10) Điều khoản bắt buộc tick
    @AssertTrue(message = "Bạn phải đồng ý với các điều khoản dịch vụ.")
    private boolean acceptTerms;

    // ===== Getters/Setters =====

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isAcceptTerms() {
        return acceptTerms;
    }

    public void setAcceptTerms(boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }
}
