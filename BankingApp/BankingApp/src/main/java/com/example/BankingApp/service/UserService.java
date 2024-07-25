package com.example.BankingApp.service;

import com.example.BankingApp.model.User;
import com.example.BankingApp.model.Role;
import com.example.BankingApp.repository.UserRepository;
import com.example.BankingApp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //@Transactional
    public boolean createUser(User user, String roleName) {
        logger.info("Attempting to create user: {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("User already exists: {}", user.getUsername());
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("Password encoded for user: {}", user.getUsername());

        try {
            User savedUser = userRepository.save(user);
            assignRoleToUser(savedUser, roleName);
            logger.info("User saved successfully. Generated ID: {}", savedUser.getId());
            return true;
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            return false;
        }
    }

    private void assignRoleToUser(User user, String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role != null) {
            user.getRoles().add(role);
            userRepository.save(user);
            logger.info("Role {} assigned to user {}", roleName, user.getUsername());
        } else {
            logger.warn("Role {} not found", roleName);
        }
    }
}
