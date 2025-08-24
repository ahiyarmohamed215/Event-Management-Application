package com.mohamed.EventManagementApplication.repositories;

import com.mohamed.EventManagementApplication.domain.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
