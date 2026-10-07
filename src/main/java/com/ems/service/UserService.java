package com.ems.service;

import com.ems.dto.LoginRequest;
import com.ems.entity.User;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (userRepository.existsByEmail(user.getEmail().trim())) {
            throw new IllegalArgumentException("Email is already registered. Please login or use a different email.");
        }

        user.setEmail(user.getEmail().trim().toLowerCase());
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("USER");
        } else {
            user.setRole(user.getRole().trim().toUpperCase());
        }

        return userRepository.save(user);
    }

    public User login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().trim().toLowerCase();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
