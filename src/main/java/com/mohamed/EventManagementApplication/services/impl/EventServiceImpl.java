package com.mohamed.EventManagementApplication.services.impl;

import com.mohamed.EventManagementApplication.domain.CreateEventRequest;
import com.mohamed.EventManagementApplication.domain.entities.Event;
import com.mohamed.EventManagementApplication.domain.entities.TicketType;
import com.mohamed.EventManagementApplication.domain.entities.User;
import com.mohamed.EventManagementApplication.exceptions.UserNotFoundException;
import com.mohamed.EventManagementApplication.repositories.EventRepository;
import com.mohamed.EventManagementApplication.repositories.UserRepository;
import com.mohamed.EventManagementApplication.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest eventRequest) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with ID '%s' not found", organizerId))
                );

        Event eventToCreate = new Event();

        List<TicketType> ticketTypes = eventRequest.getTicketTypeRequests().stream().map(
                createTicketTypeRequest -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketTypeToCreate.getName());
                    ticketTypeToCreate.setPrice(ticketTypeToCreate.getPrice());
                    ticketTypeToCreate.setDescription(ticketTypeToCreate.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketTypeToCreate.getTotalAvailable());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return ticketTypeToCreate;
                }).toList();

        eventToCreate.setName(eventRequest.getName());
        eventToCreate.setStart(eventRequest.getStart());
        eventToCreate.setEnd(eventRequest.getEnd());
        eventToCreate.setVenue(eventRequest.getVenue());
        eventToCreate.setSalesStart(eventRequest.getSalesStart());
        eventToCreate.setSalesEnd(eventRequest.getSalesEnd());
        eventToCreate.setStatus(eventRequest.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypes);

        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id,organizerId);
    }
}
