package com.ems.controller;

import com.ems.dto.ApiResponse;
import com.ems.entity.Registration;
import com.ems.repository.EventRepository;
import com.ems.repository.RegistrationRepository;
import com.ems.repository.UserRepository;
import com.ems.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final RegistrationService registrationService;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(RegistrationService registrationService,
                           EventRepository eventRepository,
                           RegistrationRepository registrationRepository,
                           UserRepository userRepository) {
        this.registrationService = registrationService;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/events/{eventId}/registrations")
    public ResponseEntity<ApiResponse> getEventRegistrations(@PathVariable Long eventId) {
        List<Registration> list = registrationService.getEventRegistrations(eventId);
        return ResponseEntity.ok(ApiResponse.ok("Event registrations retrieved", list));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEvents", eventRepository.count());
        stats.put("totalRegistrations", registrationRepository.count());
        stats.put("totalUsers", userRepository.count());
        return ResponseEntity.ok(ApiResponse.ok("Admin stats retrieved", stats));
    }
}
