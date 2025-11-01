package com.medicalstore.service;

import com.medicalstore.model.User;
import java.util.List;
import java.util.Optional;

public interface AuthService {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> findAll();

    boolean registerUser(User user, String rawPassword);
    boolean updateUser(User user);
    boolean deleteUser(Long id);

    int userCount();
}