package com.medicalstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // no CSRF for dev stage
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().permitAll()
                )
                // ❌ Disable Spring Security’s form login
                .formLogin(form -> form.disable())
                // ❌ Disable logout handling (you already have manual /logout)
                .logout(logout -> logout.disable());

        return http.build();
    }
}
