package com.example.bai5.repo;

import com.example.bai5.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCustomerCode(String customerCode);
    boolean existsByEmail(String email);
}
