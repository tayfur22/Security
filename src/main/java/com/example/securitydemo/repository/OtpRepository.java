package com.example.securitydemo.repository;

import com.example.securitydemo.entity.Otp;
import com.example.securitydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {
//    Optional<Otp> findOtpByUsername(String username);
    Optional<Otp> findTopByUserOrderByIdDesc(User user);
}
