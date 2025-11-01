package com.medicalstore.repository;

import com.medicalstore.model.User;
import java.util.Optional;
import java.util.List;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    int save(User user); // returns rows inserted
    int update(User user); // returns rows updated
    int deleteById(Long id); // returns rows deleted

    int count(); // utility for test
}
