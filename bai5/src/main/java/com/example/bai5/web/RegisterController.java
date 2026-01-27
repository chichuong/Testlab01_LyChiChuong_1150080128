package com.example.bai5.web;

import com.example.bai5.domain.Gender;
import com.example.bai5.service.CustomerService;
import com.example.bai5.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    private final CustomerService service;

    public RegisterController(CustomerService service) {
        this.service = service;
    }

    @GetMapping({ "/", "/register" })
    public String showForm(Model model) {
        model.addAttribute("form", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(
            @Valid @ModelAttribute("form") RegisterRequest form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            service.register(form);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();

            if (msg.contains("Mã khách hàng")) {
                bindingResult.rejectValue("customerCode", "duplicate", msg);
            } else if (msg.contains("Email")) {
                bindingResult.rejectValue("email", "duplicate", msg);
            } else if (msg.contains("Xác nhận mật khẩu")) {
                bindingResult.rejectValue("confirmPassword", "mismatch", msg);
            } else if (msg.contains("18 tuổi")) {
                bindingResult.rejectValue("dob", "age", msg);
            } else {
                model.addAttribute("errorMessage", msg);
            }
            return "register";
        }

        model.addAttribute("successMessage", "Đăng ký tài khoản thành công!");
        model.addAttribute("form", new RegisterRequest()); // reset form sau khi thành công
        return "register";
    }

    @ModelAttribute("genders")
    public Gender[] genders() {
        return Gender.values();
    }
}
