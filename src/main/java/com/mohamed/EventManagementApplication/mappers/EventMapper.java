package com.mohamed.EventManagementApplication.mappers;

import com.mohamed.EventManagementApplication.domain.CreateEventRequest;
import com.mohamed.EventManagementApplication.domain.CreateTicketTypeRequest;
import com.mohamed.EventManagementApplication.domain.dtos.CreateEventRequestDto;
import com.mohamed.EventManagementApplication.domain.dtos.CreateEventResponseDto;
import com.mohamed.EventManagementApplication.domain.dtos.CreateTicketTypeRequestDto;
import com.mohamed.EventManagementApplication.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

}
