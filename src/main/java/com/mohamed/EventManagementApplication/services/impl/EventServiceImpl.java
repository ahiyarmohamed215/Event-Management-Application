package com.mohamed.EventManagementApplication.services.impl;

import com.mohamed.EventManagementApplication.domain.CreateEventRequest;
import com.mohamed.EventManagementApplication.domain.UpdateEventRequest;
import com.mohamed.EventManagementApplication.domain.UpdateTicketTypeRequest;
import com.mohamed.EventManagementApplication.domain.entities.Event;
import com.mohamed.EventManagementApplication.domain.entities.TicketType;
import com.mohamed.EventManagementApplication.domain.entities.User;
import com.mohamed.EventManagementApplication.exceptions.EventNotFoundException;
import com.mohamed.EventManagementApplication.exceptions.EventUpdateException;
import com.mohamed.EventManagementApplication.exceptions.TicketTypeNotFoundException;
import com.mohamed.EventManagementApplication.exceptions.UserNotFoundException;
import com.mohamed.EventManagementApplication.repositories.EventRepository;
import com.mohamed.EventManagementApplication.repositories.UserRepository;
import com.mohamed.EventManagementApplication.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest eventRequest) {
        if(null == eventRequest.getId()){
            throw new EventUpdateException("Event ID cannot be null");
        }

        if(!id.equals(eventRequest.getId())){
            throw new EventUpdateException("Cannot update the ID of an event");
        }
        Event existingevent = eventRepository
                .findByIdAndOrganizerId(id,organizerId)
                .orElseThrow(() -> new EventNotFoundException(
                String.format("Event with ID '%s' does not exist" , id)));

        existingevent.setName(eventRequest.getName());
        existingevent.setStart(eventRequest.getStart());
        existingevent.setEnd(eventRequest.getEnd());
        existingevent.setVenue(eventRequest.getVenue());
        existingevent.setSalesStart(eventRequest.getSalesStart());
        existingevent.setSalesEnd(eventRequest.getSalesEnd());
        existingevent.setStatus(eventRequest.getStatus());

         Set<UUID> requestTicketTypeIds = eventRequest.getTicketTypeRequests()
                .stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


         existingevent.getTicketTypes().removeIf(exisitngTicketType ->
                 !requestTicketTypeIds.contains(exisitngTicketType.getId())
         );

         Map<UUID,TicketType> existingTicketTypeIndex = existingevent.getTicketTypes().stream()
                 .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for (UpdateTicketTypeRequest ticketTypeRequest : eventRequest.getTicketTypeRequests()) {
            if (null == ticketTypeRequest.getId()){
                //create
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketTypeToCreate.getName());
                ticketTypeToCreate.setPrice(ticketTypeToCreate.getPrice());
                ticketTypeToCreate.setDescription(ticketTypeToCreate.getDescription());
                ticketTypeToCreate.setTotalAvailable(ticketTypeToCreate.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingevent);
                existingevent.getTicketTypes().add(ticketTypeToCreate);

            }else if (existingTicketTypeIndex.containsKey(ticketTypeRequest.getId())){
                //update
                TicketType existingTicketType = existingTicketTypeIndex.get(ticketTypeRequest.getId());
                existingTicketType.setName(ticketTypeRequest.getName());
                existingTicketType.setPrice(ticketTypeRequest.getPrice());
                existingTicketType.setDescription(ticketTypeRequest.getDescription());
                existingTicketType.setTotalAvailable(ticketTypeRequest.getTotalAvailable());

            }else {
                throw new TicketTypeNotFoundException(String.format(
                        "Ticket type with ID '%s' does not exist",ticketTypeRequest.getId()
                ));
            }

        }

        return eventRepository.save(existingevent);
    }
}
