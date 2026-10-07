package com.ems.config;

import com.ems.entity.Event;
import com.ems.entity.Registration;
import com.ems.entity.User;
import com.ems.repository.EventRepository;
import com.ems.repository.RegistrationRepository;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           EventRepository eventRepository,
                           RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    public void run(String... args) {
        // Seed default users if none exist
        if (userRepository.count() == 0) {
            User admin = new User("System Administrator", "admin@ems.com", "admin123", "ADMIN");
            User user1 = new User("John Doe", "john@example.com", "user123", "USER");
            User user2 = new User("Jane Smith", "jane@example.com", "user123", "USER");
            userRepository.save(admin);
            userRepository.save(user1);
            userRepository.save(user2);
            System.out.println("Default users seeded: admin@ems.com / admin123, john@example.com / user123");
        }

        // Seed default events if none exist
        if (eventRepository.count() == 0) {
            Event e1 = new Event(
                    "Global Tech Summit 2026",
                    "Join leading industry technologists, AI researchers, and software engineers for 2 days of keynotes, workshops, and networking.",
                    "Technology",
                    "2026-11-15",
                    "San Francisco Convention Center, CA",
                    150,
                    145
            );

            Event e2 = new Event(
                    "Indie Music Festival",
                    "An open-air acoustic music experience featuring breakout indie bands, food trucks, and art exhibits.",
                    "Music",
                    "2026-12-05",
                    "Greenwood Amphitheater, Austin, TX",
                    300,
                    250
            );

            Event e3 = new Event(
                    "UI/UX Design Masterclass",
                    "Hands-on workshop covering modern design systems, Figma workflows, and user empathy in product development.",
                    "Workshop",
                    "2026-10-25",
                    "Design Hub, Seattle, WA",
                    40,
                    12
            );

            Event e4 = new Event(
                    "Startup Pitch & Investor Meet",
                    "Early-stage founders present revolutionary ideas to venture capitalists, angel investors, and accelerators.",
                    "Business",
                    "2026-11-01",
                    "Innovation Loft, New York, NY",
                    60,
                    0 // Full capacity demonstration
            );

            Event e5 = new Event(
                    "National Gaming Championship",
                    "Competitive esports tournament featuring top teams, live commentary, VR showcases, and merchandise booths.",
                    "Gaming",
                    "2026-12-20",
                    "Metro Arena, Chicago, IL",
                    500,
                    420
            );

            eventRepository.save(e1);
            eventRepository.save(e2);
            eventRepository.save(e3);
            eventRepository.save(e4);
            eventRepository.save(e5);

            System.out.println("Sample events seeded successfully.");

            // Create a sample registration for John Doe on Event 1
            userRepository.findByEmail("john@example.com").ifPresent(user -> {
                Registration reg = new Registration(user.getId(), e1.getId());
                reg.setRegistrationDate(LocalDateTime.now().minusDays(1));
                reg.setStatus("CONFIRMED");
                registrationRepository.save(reg);
            });
        }
    }
}
