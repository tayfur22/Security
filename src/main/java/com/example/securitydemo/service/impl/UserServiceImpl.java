package com.example.securitydemo.service.impl;

import com.example.securitydemo.Twilio.TwilioService;
import com.example.securitydemo.Util.GenerateCodeUtil;
import com.example.securitydemo.entity.Otp;
import com.example.securitydemo.entity.User;
import com.example.securitydemo.repository.OtpRepository;
import com.example.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpRepository otpRepository;
    private TwilioService twilioService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           OtpRepository otpRepository,TwilioService twilioService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpRepository = otpRepository;
        this.twilioService = twilioService;
    }

    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            updatedUser.setRole(user.getRole());
            return userRepository.save(updatedUser);
        }
        return null;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void auth(User user) {
        Optional<User> o = userRepository.findByUsername(user.getUsername());
        if (o.isPresent()) {
            User u = o.get();
            if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                renewOtp(u);
            } else {
                throw new BadCredentialsException("Bad credentials.");
            }
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    private void renewOtp(User u) {
        String code = GenerateCodeUtil.generateCode();
        System.out.println("Generated OTP: " + code);

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        Optional<Otp> userOtp = otpRepository.findTopByUserOrderByIdDesc(u);
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
            otp.setExpiryTime(expiryTime);
            otpRepository.save(otp);
        } else {
            Otp otp = new Otp();
            otp.setCode(code);
            otp.setUser(u);
            otp.setExpiryTime(expiryTime);
            otpRepository.save(otp);
        }
    }

    public boolean check(String username, String codeToCheck) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) return false;

        User user = userOptional.get();
        Optional<Otp> userOtp = otpRepository.findTopByUserOrderByIdDesc(user);

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
                System.out.println("OTP expired");
                return false;
            }
            return otp.getCode().equals(codeToCheck);
        }

        return false;
    }


}

