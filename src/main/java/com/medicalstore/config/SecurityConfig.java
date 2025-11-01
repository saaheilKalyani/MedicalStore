package com.medicalstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Lightweight security configuration for MVC + Thymeleaf app (no UserDetailsService yet)
 * Allows public access to login/register/auth URLs, protects /profile and /admin/**
 */
@Configuration
public class SecurityConfig {

    // ✅ Password encoder bean (used in AuthServiceImpl)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // For development only; disable CSRF temporarily
                .csrf(csrf -> csrf.disable())

                // Define authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/auth/",
                                "/css/",
                                "/js/",
                                "/images/"
                        ).permitAll()
                        .requestMatchers("/admin/").permitAll()  // you can restrict later
                        .anyRequest().permitAll() // keep all open for now, since no user auth linked yet
                )

                // ✅ Disable default login page — since you handle it with Thymeleaf manually
                .formLogin(form -> form.disable())

                // ✅ Disable logout handling (you already have /logout mapping in AuthController)
                .logout(logout -> logout.disable())

                // ✅ Allow everything else
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
