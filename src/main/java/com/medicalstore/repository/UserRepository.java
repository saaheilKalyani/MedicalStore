package com.medicalstore.repository;

import com.medicalstore.model.User;
import java.util.Optional;
import java.util.List;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    int save(User user);
    int update(User user);
    int deleteById(Long id);
    int count();
    Integer findUserIdByUsername(String username);
}
