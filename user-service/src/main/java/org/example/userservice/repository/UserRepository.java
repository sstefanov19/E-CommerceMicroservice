package org.example.userservice.repository;

import org.example.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

     Optional<User> findByUsername(String username);
}
