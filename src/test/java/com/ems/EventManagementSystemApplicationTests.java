package com.ems;

import com.ems.dto.LoginRequest;
import com.ems.entity.Event;
import com.ems.entity.Registration;
import com.ems.entity.User;
import com.ems.service.EventService;
import com.ems.service.RegistrationService;
import com.ems.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventManagementSystemApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @Test
    void contextLoads() {
        assertNotNull(userService);
        assertNotNull(eventService);
        assertNotNull(registrationService);
    }

    @Test
    void testUserFlow() {
        String testEmail = "testuser_" + System.currentTimeMillis() + "@ems.com";
        User user = new User("Test User", testEmail, "pass123", "USER");
        User saved = userService.registerUser(user);
        assertNotNull(saved.getId());

        // Test login
        LoginRequest req = new LoginRequest(testEmail, "pass123");
        User loggedIn = userService.login(req);
        assertEquals(saved.getId(), loggedIn.getId());
    }

    @Test
    void testEventAndRegistrationFlow() {
        Event event = new Event(
                "Test Event " + System.currentTimeMillis(),
                "Description",
                "Technology",
                "2026-11-20",
                "Online",
                5,
                5
        );
        Event savedEvent = eventService.createEvent(event);
        assertNotNull(savedEvent.getId());
        assertEquals(5, savedEvent.getAvailableSeats());

        // Register user
        String email = "attendee_" + System.currentTimeMillis() + "@ems.com";
        User attendee = userService.registerUser(new User("Attendee", email, "secret", "USER"));

        Registration reg = registrationService.register(attendee.getId(), savedEvent.getId());
        assertNotNull(reg.getId());
        assertEquals("CONFIRMED", reg.getStatus());

        // Check seat decremented
        Event updatedEvent = eventService.getEventById(savedEvent.getId()).orElseThrow();
        assertEquals(4, updatedEvent.getAvailableSeats());

        // Cancel registration
        Registration cancelled = registrationService.cancelRegistration(reg.getId());
        assertEquals("CANCELLED", cancelled.getStatus());

        // Check seat restored
        Event restoredEvent = eventService.getEventById(savedEvent.getId()).orElseThrow();
        assertEquals(5, restoredEvent.getAvailableSeats());
    }

    @Test
    void testSearchEvents() {
        List<Event> results = eventService.searchEvents("Tech", null, null);
        assertNotNull(results);
    }
}
