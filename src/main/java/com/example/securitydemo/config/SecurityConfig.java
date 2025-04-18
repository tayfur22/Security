package com.example.securitydemo.config;

import com.example.securitydemo.repository.impl.CustomCsrfTokenRepository;
import com.example.securitydemo.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

//    private final UserDetailsServiceImpl userDetailsService;
//    private AuthFilter authFilter;
//    private final StaticKeyAuthenticationFilter staticKeyAuthenticationFilter;
//    private final LoggingFilter loggingFilter;
//    private final CsrfTokenRepository csrfTokenRepository;
//
//    @Autowired
//    public SecurityConfig(UserDetailsServiceImpl userDetailsService,AuthFilter authFilter,
//                          StaticKeyAuthenticationFilter staticKeyAuthenticationFilter,
//                          LoggingFilter loggingFilter,
//                          CustomCsrfTokenRepository csrfTokenRepository) {
//        this.userDetailsService = userDetailsService;
//        this.authFilter = authFilter;
//        this.staticKeyAuthenticationFilter = staticKeyAuthenticationFilter;
//        this.loggingFilter = loggingFilter;
//        this.csrfTokenRepository = csrfTokenRepository;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().and()
//        http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(csrfTokenRepository)
//                        .ignoringRequestMatchers("/ciao") // əgər hansısa endpoint-i çıxmaq istəyirsənsə
//                )
//                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/users/getAll").hasRole("ADMIN")
//////                                .requestMatchers("users/getAll").hasAuthority("WRITE")
////                .requestMatchers("/users/findByUsername").hasRole("USER")
//                                .anyRequest().authenticated()
//                )
        http
                .csrf(csrf -> csrf.disable()) // CSRF qorunması deaktiv edilir
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
//                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(staticKeyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(loggingFilter, LogoutFilter.class)
//                .formLogin(withDefaults()
//                );
                .httpBasic();
//                .and()
//                .formLogin(withDefaults())
//                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
//                .sessionManagement(session -> session.sessionCreationPolicy(
//                        SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }

//    @Bean
//    public CsrfTokenRepository customCsrfTokenRepository() {
//        return new CustomCsrfTokenRepository();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new NoOpPasswordEncoder().ge;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // plain text üçün
    }


//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return new ProviderManager(authProvider);
//    }
}
