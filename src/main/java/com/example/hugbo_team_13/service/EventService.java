package com.example.hugbo_team_13.service;


import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.persistence.entity.EventEntity;
import com.example.hugbo_team_13.persistence.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing events.
 * Provides methods to create, retrieve, update, and delete events using an EventRepository.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
        /**
     * Constructor to inject the EventRepository.
     * 
     * @param eventRepository the repository to handle event data.
     */
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

      /**
     * Creates a new event from the given DTO. 
     * Validates that the event name is not empty and does not already exist.
     * 
     * @param dto the data transfer object (DTO) representing the event.
     * @return the created EventDTO object.
     * @throws IllegalArgumentException if the event name is empty.
     * @throws RuntimeException if the event name already exists.
     */
    public EventDTO createEvent(EventDTO dto) {
        // Todo: Validate input
        String name = dto.getName();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
        if (eventRepository.existsByName(name)) {
            // logger.warn("Attempted to create event with existing event name: {}", name);
            throw new RuntimeException("Event name already exists");
        }

        // Create EventEntity
        EventEntity event = convertToEntity(dto);
        EventEntity savedEvent = eventRepository.save(event);

        // Return a new EventDTO
        return convertToDTO(savedEvent);
    }
     /**
     * Retrieves an event by its ID.
     * 
     * @param id the ID of the event.
     * @return an Optional containing the EventDTO if found, or empty if not.
     */
    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(this::convertToDTO);
    }
      /**
     * Retrieves an event by its name.
     * 
     * @param name the name of the event.
     * @return the EventDTO if found, or null if no event is found.
     */
    public EventDTO getEventByName(String name) {
        EventEntity event = eventRepository.findByName(name);
        return event != null ? convertToDTO(event) : null;
    }
     /**
     * Retrieves all events.
     * 
     * @return a list of EventDTOs representing all events.
     */
    public List<EventDTO> getAllEvents() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();

        for (EventEntity event : eventEntities) {
            eventDTOs.add(convertToDTO(event));
        }
        return eventDTOs;
    }

    /**
     * Saves or updates an event based on the given DTO. 
     * If the event ID is not set, a new event is created.
     * 
     * @param dto the data transfer object (DTO) representing the event.
     * @return the saved or updated EventDTO object.
     */
    public EventDTO saveEvent(EventDTO dto) { // (save creates a new entity if ID is not set)
        EventEntity event = convertToEntity(dto);
        EventEntity savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }
    /**
     * Deletes an event by its ID.
     * 
     * @param id the ID of the event to delete.
     */
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    /**
     * Deletes all events from the database.
     */
    public void deleteAllEvents() {
        eventRepository.deleteAll();
    }
    /**
     * Converts an EventEntity to an EventDTO.
     * 
     * @param entity the EventEntity to convert.
     * @return the corresponding EventDTO.
     */
    private EventDTO convertToDTO(EventEntity entity) {
        EventDTO dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStartDate(entity.getStartDateTime().toLocalDate());
        dto.setEndDate(entity.getEndDateTime().toLocalDate());
        dto.setStartTime(entity.getStartDateTime().toLocalTime());
        dto.setEndTime(entity.getEndDateTime().toLocalTime());
        return dto;
    }
     /**
     * Converts an EventDTO to an EventEntity.
     * 
     * @param dto the EventDTO to convert.
     * @return the corresponding EventEntity.
     */
    private EventEntity convertToEntity(EventDTO dto) {
        EventEntity entity = new EventEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStartDateTime(combineDateAndTime(dto.getStartDate(), dto.getStartTime()));
        entity.setEndDateTime(combineDateAndTime(dto.getEndDate(), dto.getEndTime()));
        return entity;
    }
     /**
     * Combines a LocalDate and a LocalTime into a LocalDateTime.
     * 
     * @param date the date part.
     * @param time the time part.
     * @return the combined LocalDateTime object.
     */
    private LocalDateTime combineDateAndTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }
}
