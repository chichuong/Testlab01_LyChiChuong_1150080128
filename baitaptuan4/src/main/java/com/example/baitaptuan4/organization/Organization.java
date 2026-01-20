package com.example.baitaptuan4.organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orgId;

    @Column(nullable = false, length = 255)
    private String orgName;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @PrePersist
    void prePersist() {
        if (createdDate == null)
            createdDate = LocalDateTime.now();
    }
}
