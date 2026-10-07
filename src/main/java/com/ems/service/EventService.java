package com.ems.service;

import com.ems.entity.Event;
import com.ems.entity.Registration;
import com.ems.repository.EventRepository;
import com.ems.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public EventService(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Event title is required");
        }
        if (event.getCategory() == null || event.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Event category is required");
        }
        if (event.getDate() == null || event.getDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Event date is required");
        }
        if (event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Event location is required");
        }
        if (event.getCapacity() == null || event.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }

        if (event.getAvailableSeats() == null) {
            event.setAvailableSeats(event.getCapacity());
        }

        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));

        if (updatedEvent.getTitle() != null && !updatedEvent.getTitle().trim().isEmpty()) {
            existing.setTitle(updatedEvent.getTitle().trim());
        }
        if (updatedEvent.getDescription() != null) {
            existing.setDescription(updatedEvent.getDescription());
        }
        if (updatedEvent.getCategory() != null && !updatedEvent.getCategory().trim().isEmpty()) {
            existing.setCategory(updatedEvent.getCategory().trim());
        }
        if (updatedEvent.getDate() != null && !updatedEvent.getDate().trim().isEmpty()) {
            existing.setDate(updatedEvent.getDate().trim());
        }
        if (updatedEvent.getLocation() != null && !updatedEvent.getLocation().trim().isEmpty()) {
            existing.setLocation(updatedEvent.getLocation().trim());
        }

        if (updatedEvent.getCapacity() != null && updatedEvent.getCapacity() > 0) {
            int registeredCount = existing.getCapacity() - existing.getAvailableSeats();
            existing.setCapacity(updatedEvent.getCapacity());
            int newAvailable = updatedEvent.getCapacity() - registeredCount;
            existing.setAvailableSeats(Math.max(0, newAvailable));
        }

        return eventRepository.save(existing);
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException("Event not found with ID: " + id);
        }
        // Also remove registrations related to this event
        List<Registration> registrations = registrationRepository.findByEventId(id);
        registrationRepository.deleteAll(registrations);
        eventRepository.deleteById(id);
    }

    public List<Event> searchEvents(String title, String category, String location) {
        String cleanTitle = (title != null && !title.trim().isEmpty()) ? title.trim() : null;
        String cleanCategory = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
        String cleanLocation = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        return eventRepository.searchEvents(cleanTitle, cleanCategory, cleanLocation);
    }
}
