package com.medicalstore.service.impl;

import com.medicalstore.model.User;
import com.medicalstore.repository.UserRepository;
import com.medicalstore.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean registerUser(User user, String rawPassword) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false; // username already exists
        }
        user.setPassword(passwordEncoder.encode(rawPassword));
        if (user.getRole() == null) user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            userRepository.findById(user.getId()).ifPresent(u -> user.setPassword(u.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.update(user) > 0;
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id) > 0;
    }

    @Override
    public int userCount() {
        return userRepository.count();
    }
}