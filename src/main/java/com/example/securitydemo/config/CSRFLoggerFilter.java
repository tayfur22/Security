package com.example.securitydemo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Supplier;

@Component
public class CSRFLoggerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Object csrfAttr = request.getSession().getAttribute("_csrf");
        if (csrfAttr instanceof Supplier) {

            @SuppressWarnings("unchecked")
            Supplier<CsrfToken> csrfSuppiler = (Supplier<CsrfToken>) csrfAttr;
            CsrfToken csrfToken = csrfSuppiler.get();
            System.out.println("CSRF Token: " + csrfToken.getToken());
        } else if (csrfAttr instanceof CsrfToken) {
            CsrfToken csrfToken = (CsrfToken) csrfAttr;
            System.out.println("CSRF Token: " + csrfToken.getToken());
        }
        filterChain.doFilter(request, response);
    }
}
