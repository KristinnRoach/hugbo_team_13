package com.example.hugbo_team_13.persistence.repository;

import com.example.hugbo_team_13.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link EventEntity} objects in the database.
 * Extends {@link JpaRepository} to provide basic CRUD operations for events.
 * Includes custom queries for finding events by name and checking for existence by name.
 */
@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    /**
     * Finds an {@link EventEntity} by its name.
     *
     * @param name the name of the event.
     * @return the {@link EventEntity} associated with the given name, or null if no event is found.
     * Consider returning an {@link Optional} to avoid potential null values.
     */
    EventEntity findByName(String name); // Optional<EventEntity> would be better to handle nulls safely.

    /**
     * Checks if an event exists in the database with the specified name.
     *
     * @param name the name to check for.
     * @return true if an event with the specified name exists, false otherwise.
     */
    boolean existsByName(String name);

    List<EventEntity> findAllByStartDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<EventEntity> findByGameId(Long gameId);

    List<EventEntity> findByStartDateTimeBetweenAndGameId(LocalDateTime startDateTime, LocalDateTime endDateTime, Long id);
}
