package com.mohamed.EventManagementApplication.repositories;

import com.mohamed.EventManagementApplication.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<User, UUID> {
}
