package com.ems.controller;

import com.ems.dto.ApiResponse;
import com.ems.dto.RegistrationRequest;
import com.ems.entity.Registration;
import com.ems.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> registerForEvent(@Valid @RequestBody RegistrationRequest request) {
        try {
            Registration reg = registrationService.register(request.getUserId(), request.getEventId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Registration successful!", reg));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration error: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserRegistrations(@PathVariable Long userId) {
        List<Registration> list = registrationService.getUserRegistrations(userId);
        return ResponseEntity.ok(ApiResponse.ok("Fetched user registrations", list));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> cancelRegistration(@PathVariable Long id) {
        try {
            Registration reg = registrationService.cancelRegistration(id);
            return ResponseEntity.ok(ApiResponse.ok("Registration cancelled successfully. Seat has been released.", reg));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to cancel registration: " + e.getMessage()));
        }
    }
}
