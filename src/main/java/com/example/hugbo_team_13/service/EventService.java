package com.example.hugbo_team_13.service;


import com.example.hugbo_team_13.model.EventDTO;
import com.example.hugbo_team_13.persistence.entity.EventEntity;
import com.example.hugbo_team_13.persistence.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

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

    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(this::convertToDTO);
    }

    public EventDTO getEventByName(String name) {
        EventEntity event = eventRepository.findByName(name);
        return event != null ? convertToDTO(event) : null;
    }

    public List<EventDTO> getAllEvents() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();

        for (EventEntity event : eventEntities) {
            eventDTOs.add(convertToDTO(event));
        }
        return eventDTOs;
    }


    public EventDTO saveEvent(EventDTO dto) { // (save creates a new entity if ID is not set)
        EventEntity event = convertToEntity(dto);
        EventEntity savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public void deleteAllEvents() {
        eventRepository.deleteAll();
    }

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

    private EventEntity convertToEntity(EventDTO dto) {
        EventEntity entity = new EventEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStartDateTime(combineDateAndTime(dto.getStartDate(), dto.getStartTime()));
        entity.setEndDateTime(combineDateAndTime(dto.getEndDate(), dto.getEndTime()));
        return entity;
    }

    private LocalDateTime combineDateAndTime(LocalDate date, LocalTime time) {
        // LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        // LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ISO_TIME);
        return LocalDateTime.of(date, time);
    }
}


/*

/**
 * Combines a date string and a time string into a Date object.
 * @param {string} dateStr - Date string in YYYY-MM-DD format.
 * @param {string} timeStr - Time string in HH:MM format.
 * @returns {Date} Combined Date object.
 */

/*
function combineDateAndTime(dateStr, timeStr) {
    const [year, month, day] = dateStr.split('-').map(Number);
    const [hours, minutes] = timeStr.split(':').map(Number);
    return new Date(year, month - 1, day, hours, minutes);
}


      */