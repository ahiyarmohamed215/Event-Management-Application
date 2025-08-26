package com.mohamed.EventManagementApplication.services;

import com.mohamed.EventManagementApplication.domain.CreateEventRequest;
import com.mohamed.EventManagementApplication.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest eventRequest);
    Page<Event> listEventsForOrganizer (UUID organizerId, Pageable pageable);
    Optional<Event> getEventForOrganizer (UUID organizerId,UUID id);
}
