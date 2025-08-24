package com.mohamed.EventManagementApplication.services;

import com.mohamed.EventManagementApplication.domain.CreateEventRequest;
import com.mohamed.EventManagementApplication.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest eventRequest);
}
