package com.example.securitydemo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class LoggingFilter implements Filter{

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Optional initialization logic, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        logger.info("Incoming request: method={}, url={}, headers={}",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                getHeaders(httpRequest));

        chain.doFilter(request, response);

        logger.info("Response status: {}", httpResponse.getStatus());
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            headers.append(headerName)
                    .append(": ")
                    .append(request.getHeader(headerName))
                    .append("; ");
        });
        return headers.toString();
    }

}