package com.example.demo.controller;

import com.example.demo.model.RegistrationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @GetMapping("/dang-ky")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "dang-ky";
    }

    @PostMapping("/dang-ky")
    public String processRegistration(@Valid RegistrationForm registrationForm,
            BindingResult bindingResult, Model model) {
        // Kiểm tra xác nhận mật khẩu trùng khớp
        if (registrationForm.getMatKhau() != null
                && !registrationForm.getMatKhau().equals(registrationForm.getXacNhanMatKhau())) {
            bindingResult.rejectValue("xacNhanMatKhau", "error.xacNhanMatKhau",
                    "Xác nhận mật khẩu không trùng khớp với Mật khẩu");
        }

        // Kiểm tra ngày sinh (nếu có) — tuổi từ 16 đến dưới 100
        if (registrationForm.getNgaySinh() != null && !registrationForm.getNgaySinh().isBlank()) {
            try {
                String[] parts = registrationForm.getNgaySinh().split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                java.time.LocalDate dob = java.time.LocalDate.of(year, month, day);
                java.time.LocalDate today = java.time.LocalDate.now();
                int age = java.time.Period.between(dob, today).getYears();
                if (age < 16 || age >= 100) {
                    bindingResult.rejectValue("ngaySinh", "error.ngaySinh",
                            "Phải từ 16 tuổi đến dưới 100 tuổi tính đến ngày đăng ký");
                }
            } catch (Exception e) {
                bindingResult.rejectValue("ngaySinh", "error.ngaySinh",
                        "Ngày sinh không hợp lệ (dd/mm/yyyy)");
            }
        }

        if (bindingResult.hasErrors()) {
            return "dang-ky";
        }

        model.addAttribute("success", true);
        return "dang-ky";
    }

    @GetMapping("/dang-nhap")
    public String showLoginPage() {
        return "dang-nhap";
    }
}
