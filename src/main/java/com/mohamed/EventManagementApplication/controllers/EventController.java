package com.mohamed.EventManagementApplication.controllers;

import com.mohamed.EventManagementApplication.domain.CreateEventRequest;
import com.mohamed.EventManagementApplication.domain.dtos.CreateEventRequestDto;
import com.mohamed.EventManagementApplication.domain.dtos.CreateEventResponseDto;
import com.mohamed.EventManagementApplication.domain.entities.Event;
import com.mohamed.EventManagementApplication.mappers.EventMapper;
import com.mohamed.EventManagementApplication.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path ="/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto){
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID userId =UUID.fromString(jwt.getSubject());

        Event createEvent = eventService.createEvent(userId,createEventRequest);
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createEvent);
        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);
    }
}