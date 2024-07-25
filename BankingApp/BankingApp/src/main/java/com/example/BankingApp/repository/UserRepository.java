package com.example.BankingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.BankingApp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
