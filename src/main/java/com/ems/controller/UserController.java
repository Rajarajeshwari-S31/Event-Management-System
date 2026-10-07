package com.ems.controller;

import com.ems.dto.ApiResponse;
import com.ems.dto.LoginRequest;
import com.ems.entity.User;
import com.ems.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        try {
            User registered = userService.registerUser(user);
            // Hide password in response
            registered.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("User registered successfully!", registered));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to register user: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest);
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole());

            return ResponseEntity.ok(ApiResponse.ok("Login successful!", userData));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(u -> {
                    u.setPassword(null);
                    return ResponseEntity.ok(ApiResponse.ok("User found", u));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found with ID: " + id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        var users = userService.findAll();
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(ApiResponse.ok("Fetched all users", users));
    }
}
