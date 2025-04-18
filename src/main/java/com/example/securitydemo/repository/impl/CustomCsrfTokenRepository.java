package com.example.securitydemo.repository.impl;

import com.example.securitydemo.entity.Token;
import com.example.securitydemo.entity.User;
import com.example.securitydemo.repository.JpaTokenRepository;
import com.example.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomCsrfTokenRepository implements CsrfTokenRepository {

    private final JpaTokenRepository jpaTokenRepository;
    private final UserRepository userRepository;


    @Autowired
    public CustomCsrfTokenRepository(JpaTokenRepository jpaTokenRepository,
                                     UserRepository userRepository) {
        this.jpaTokenRepository = jpaTokenRepository;
        this.userRepository = userRepository;
    }



//    @Autowired
//    private JpaTokenRepository jpaTokenRepository;
//
//    @Autowired
//    private UserRepository userRepository;

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String uuid = UUID.randomUUID().toString();
        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", uuid);
    }


    @Override
    public void saveToken(CsrfToken csrfToken,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        String identifier = request.getHeader("X-IDENTIFIER");
        if (identifier == null) return;

        // User mövcud deyilsə, heç bir token saxlanmasın
        Optional<User> userOptional = userRepository.findByUsername(identifier);
        if (userOptional.isEmpty()) return;

        Optional<Token> existingToken = jpaTokenRepository.findTokenByIdentifier(identifier);

        if (existingToken.isPresent()) {
            Token token = existingToken.get();
            token.setToken(csrfToken.getToken());
            jpaTokenRepository.save(token);
        } else {
            Token token = new Token();
            token.setToken(csrfToken.getToken());
            token.setIdentifier(identifier);
            token.setUser(userOptional.get());

            jpaTokenRepository.save(token);
        }
    }


    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String identifier = request.getHeader("X-IDENTIFIER");
        Optional<Token> existingToken = jpaTokenRepository.findTokenByIdentifier(identifier);
        if (existingToken.isPresent()) {
            Token token = existingToken.get();
            return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token.getToken());
        }
        return null;

    }
}