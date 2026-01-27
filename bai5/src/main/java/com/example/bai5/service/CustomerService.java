package com.example.bai5.service;

import com.example.bai5.domain.Customer;
import com.example.bai5.repo.CustomerRepository;
import com.example.bai5.web.dto.RegisterRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public void register(RegisterRequest req) {

        // Unique check (mềm)
        if (repo.existsByCustomerCode(req.getCustomerCode())) {
            throw new IllegalArgumentException("Mã khách hàng đã tồn tại.");
        }
        if (repo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }

        // Confirm password
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("Xác nhận mật khẩu không khớp.");
        }

        // DOB >= 18 nếu có nhập
        if (req.getDob() != null) {
            int years = Period.between(req.getDob(), LocalDate.now()).getYears();
            if (years < 18) {
                throw new IllegalArgumentException("Bạn phải đủ 18 tuổi.");
            }
        }

        Customer c = new Customer();
        c.setCustomerCode(req.getCustomerCode().trim());
        c.setFullName(req.getFullName().trim());
        c.setEmail(req.getEmail().trim().toLowerCase());
        c.setPhone(req.getPhone().trim());
        c.setAddress(req.getAddress().trim());
        c.setDob(req.getDob());
        c.setGender(req.getGender());
        c.setPasswordHash(encoder.encode(req.getPassword()));

        try {
            repo.save(c);
        } catch (DataIntegrityViolationException e) {
            // phòng trường hợp trùng do race-condition
            throw new IllegalArgumentException("Mã khách hàng hoặc Email đã tồn tại (trùng dữ liệu).");
        }
    }
}
