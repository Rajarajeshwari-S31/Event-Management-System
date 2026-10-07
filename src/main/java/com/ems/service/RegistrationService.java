package com.ems.service;

import com.ems.entity.Event;
import com.ems.entity.Registration;
import com.ems.entity.User;
import com.ems.repository.EventRepository;
import com.ems.repository.RegistrationRepository;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository,
                               EventRepository eventRepository,
                               UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Registration register(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        // Check if user is already registered for this event
        boolean alreadyRegistered = registrationRepository.existsByUserIdAndEventIdAndStatus(userId, eventId, "CONFIRMED");
        if (alreadyRegistered) {
            throw new IllegalStateException("You are already registered for this event.");
        }

        // Check capacity
        if (event.getAvailableSeats() == null || event.getAvailableSeats() <= 0) {
            throw new IllegalStateException("Registration failed: Event is fully booked! No seats available.");
        }

        // Decrement available seats
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        // Check if there is an existing cancelled registration to reactivate or create new
        Optional<Registration> existingOpt = registrationRepository.findByUserIdAndEventId(userId, eventId);
        Registration registration;
        if (existingOpt.isPresent()) {
            registration = existingOpt.get();
            registration.setStatus("CONFIRMED");
            registration.setRegistrationDate(LocalDateTime.now());
        } else {
            registration = new Registration();
            registration.setUserId(userId);
            registration.setEventId(eventId);
            registration.setStatus("CONFIRMED");
            registration.setRegistrationDate(LocalDateTime.now());
        }

        Registration saved = registrationRepository.save(registration);

        // Populate transient fields
        saved.setUserName(user.getName());
        saved.setUserEmail(user.getEmail());
        saved.setEventTitle(event.getTitle());
        saved.setEventCategory(event.getCategory());
        saved.setEventDate(event.getDate());
        saved.setEventLocation(event.getLocation());

        return saved;
    }

    public List<Registration> getUserRegistrations(Long userId) {
        List<Registration> registrations = registrationRepository.findByUserId(userId);
        for (Registration reg : registrations) {
            eventRepository.findById(reg.getEventId()).ifPresent(event -> {
                reg.setEventTitle(event.getTitle());
                reg.setEventCategory(event.getCategory());
                reg.setEventDate(event.getDate());
                reg.setEventLocation(event.getLocation());
            });
            userRepository.findById(reg.getUserId()).ifPresent(u -> {
                reg.setUserName(u.getName());
                reg.setUserEmail(u.getEmail());
            });
        }
        return registrations;
    }

    @Transactional
    public Registration cancelRegistration(Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found with ID: " + id));

        if ("CANCELLED".equalsIgnoreCase(registration.getStatus())) {
            throw new IllegalStateException("Registration is already cancelled.");
        }

        registration.setStatus("CANCELLED");
        registrationRepository.save(registration);

        // Increment event available seats
        eventRepository.findById(registration.getEventId()).ifPresent(event -> {
            if (event.getAvailableSeats() < event.getCapacity()) {
                event.setAvailableSeats(event.getAvailableSeats() + 1);
                eventRepository.save(event);
            }
        });

        // Populate metadata
        eventRepository.findById(registration.getEventId()).ifPresent(event -> {
            registration.setEventTitle(event.getTitle());
            registration.setEventCategory(event.getCategory());
            registration.setEventDate(event.getDate());
            registration.setEventLocation(event.getLocation());
        });

        return registration;
    }

    public List<Registration> getEventRegistrations(Long eventId) {
        List<Registration> registrations = registrationRepository.findByEventId(eventId);
        for (Registration reg : registrations) {
            userRepository.findById(reg.getUserId()).ifPresent(user -> {
                reg.setUserName(user.getName());
                reg.setUserEmail(user.getEmail());
            });
            eventRepository.findById(reg.getEventId()).ifPresent(event -> {
                reg.setEventTitle(event.getTitle());
            });
        }
        return registrations;
    }
}
