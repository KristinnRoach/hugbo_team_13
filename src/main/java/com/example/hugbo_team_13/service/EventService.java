package com.example.hugbo_team_13.service;


import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.GameDTO;
//import com. example. hugbo_team_13.exception._ResourceAlreadyExistsException;
import com.example.hugbo_team_13.persistence.entity.EventEntity;
import com.example.hugbo_team_13.persistence.entity.GameEntity;
import com.example.hugbo_team_13.persistence.repository.EventRepository;
import com.example.hugbo_team_13.persistence.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing events.
 * Provides methods to create, retrieve, update, and delete events using an EventRepository.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final GameService gameService;
    private final GameRepository gameRepository;

    /**
     * Constructor to inject the EventRepository.
     *
     * @param eventRepository the repository to handle event data.
     */
    public EventService(EventRepository eventRepository, UserService userService, GameService gameService, GameRepository gameRepository) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    /**
     * Creates a new event from the given DTO.
     * Validates that the event name is not empty and does not already exist.
     *
     * @param dto the data transfer object (DTO) representing the event.
     * @return the created EventDTO object.
     * @throws IllegalArgumentException if the event name is empty.
     * @throws RuntimeException         if the event name already exists.
     */
    public EventDTO createEvent(EventDTO dto) throws Exception {
        String name = dto.getName();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
        if (eventRepository.existsByName(name)) {
           throw new Exception("Event name already exists"); // ResourceAlreadyExistsException("Event name already exists");
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
    public Optional<EventDTO> getEventById(String id) {
        return eventRepository.findById(Long.valueOf(id)).map(this::convertToDTO);
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
    @Transactional
    public void deleteEvent(String id) {
        EventEntity event = eventRepository.findById(Long.valueOf(id))
                .orElseThrow(); // todo: -> new EntityNotFoundException("Event not found"));

        // Remove the event from all users attendingEvents sets
        event.getAttendees().forEach(user -> {
            user.getAttendingEvents().remove(event);
        });

        // Clear the attendees set
        event.getAttendees().clear();

        // delete the event
        eventRepository.delete(event);
    }

    public List<EventDTO> findEventsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<EventEntity> entities = eventRepository.findAllByStartDateTimeBetween(startDateTime, endDateTime);
        return entities.stream().map(this::convertToDTO).toList();
    }


    /**
     * Deletes all events from the database.
     */
    private void deleteAllEvents() {
        eventRepository.deleteAll();
    }

    /**
     *
     * @param entity the EventEntity to convert.
     * @return the corresponding EventDTO.
     */
    private EventDTO convertToDTO(EventEntity entity) {
        EventDTO dto = new EventDTO();
        dto.setId(entity.getId().toString());
        dto.setName(entity.getName());
        dto.setStartDate(entity.getStartDateTime().toLocalDate());
        dto.setEndDate(entity.getEndDateTime().toLocalDate());
        dto.setStartTime(entity.getStartDateTime().toLocalTime());
        dto.setEndTime(entity.getEndDateTime().toLocalTime());

        GameEntity gameEntity = entity.getGame();
        GameDTO gameDTO = gameService.getGameById(String.valueOf(gameEntity.getId())).orElseThrow();
        dto.setGame(gameDTO);

        dto.setAttendees(entity.getAttendees().stream()
                .map(userService::convertToDTO)
                .collect(Collectors.toSet()));

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
        if (dto.getId() != null) {
            entity.setId(Long.parseLong(dto.getId()));
        }
        entity.setName(dto.getName());
        entity.setStartDateTime(combineDateAndTime(dto.getStartDate(), dto.getStartTime()));
        entity.setEndDateTime(combineDateAndTime(dto.getEndDate(), dto.getEndTime()));

        // extract the game entity
        GameEntity game = gameRepository.findByName(dto.getGame().getName());
        if (game == null) {
            throw new RuntimeException("Game not found: " + dto.getGame());
        }
        entity.setGame(game);

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
